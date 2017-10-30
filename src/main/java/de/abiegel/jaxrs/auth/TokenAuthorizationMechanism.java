package de.abiegel.jaxrs.auth;

import java.security.Key;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.UUID;

import javax.crypto.spec.SecretKeySpec;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.enterprise.AuthenticationException;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import javax.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStoreHandler;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@ApplicationScoped
public class TokenAuthorizationMechanism implements HttpAuthenticationMechanism {

	@Inject
	IdentityStoreHandler handler;

	@Context
	private UriInfo uriInfo;

	@Override
	public AuthenticationStatus validateRequest(HttpServletRequest request, HttpServletResponse response,
			HttpMessageContext httpMessageContext) throws AuthenticationException {

		if (httpMessageContext.isAuthenticationRequest()) {
			return issueingPhase(request, response, httpMessageContext);
		}

		if (httpMessageContext.isProtected()) {
			return validationPhase(request, httpMessageContext);
		}

		return httpMessageContext.doNothing();

	}

	private AuthenticationStatus validationPhase(HttpServletRequest request, HttpMessageContext httpMessageContext) {
		// Get the HTTP Authorization header from the request
		String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (authorizationHeader == null || authorizationHeader.isEmpty())

			return httpMessageContext.responseUnauthorized();

		// Extract the token from the HTTP Authorization header
		String token = authorizationHeader.substring("Bearer".length()).trim();

		try {

			// Validate the token
			Key key = generateKey();
			Jws<Claims> claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token);

			return httpMessageContext.notifyContainerAboutLogin(claims.getBody().getSubject(),
					new HashSet<>(Arrays.asList(claims.getBody().getSubject())));

		} catch (Exception e) {
			e.printStackTrace();
			return httpMessageContext.responseUnauthorized();
		}
	}

	private AuthenticationStatus issueingPhase(HttpServletRequest request, HttpServletResponse response,
			HttpMessageContext httpMessageContext) {

		CredentialValidationResult result = this.handler
				.validate(httpMessageContext.getAuthParameters().getCredential());
		if (result.getStatus() == CredentialValidationResult.Status.VALID) {
			String token = issueTokenFor(result.getCallerPrincipal().getName());
			response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
			return httpMessageContext.notifyContainerAboutLogin(result.getCallerPrincipal(), result.getCallerGroups());
		}

		return httpMessageContext.responseUnauthorized();

	}

	public static Key generateKey() {
		String keyString = "simplekey";
		Key key = new SecretKeySpec(keyString.getBytes(), 0, keyString.getBytes().length, "DES");
		return key;
	}

	private String issueTokenFor(String user) {

		Key key = generateKey();
		String jwt = Jwts.builder().setSubject(user).setIssuer(uriInfo.getAbsolutePath().toString())
				.setIssuedAt(Date.from(Instant.now())).setId(UUID.randomUUID().toString())
				.setExpiration(Date.from(Instant.now().plusSeconds(120l))).signWith(SignatureAlgorithm.HS512, key)
				.compact();
		return jwt;
	}
}
