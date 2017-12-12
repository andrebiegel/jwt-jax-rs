package de.abiegel.jaxrs.auth.base;

import static javax.security.auth.message.AuthStatus.SEND_SUCCESS;
import static javax.security.auth.message.AuthStatus.SUCCESS;

import java.io.IOException;
import java.security.Key;
import java.security.Principal;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

import javax.crypto.spec.SecretKeySpec;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.AuthStatus;
import javax.security.auth.message.MessageInfo;
import javax.security.auth.message.MessagePolicy;
import javax.security.auth.message.callback.CallerPrincipalCallback;
import javax.security.auth.message.callback.GroupPrincipalCallback;
import javax.security.auth.message.module.ServerAuthModule;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;

import de.abiegel.jaxrs.auth.AuthNStore;
import de.abiegel.jaxrs.auth.AuthParams;
import de.abiegel.jaxrs.auth.AuthZStore;
import de.abiegel.jaxrs.auth.AuthenticationStatus;
import de.abiegel.jaxrs.auth.Jaspic;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * Very basic SAM that returns a single hardcoded user named "test" with role "architect" when the request parameter
 * <code>doLogin</code> is present.
 * 
 * @author Arjan Tijms
 * 
 */
public class TestServerAuthModule implements ServerAuthModule {

    private CallbackHandler handler;
    private AuthNStore authn = new AuthNStore();
    private AuthZStore authz = new AuthZStore();
    private Class<?>[] supportedMessageTypes = new Class[] { HttpServletRequest.class, HttpServletResponse.class };

    @Override
    public void initialize(MessagePolicy requestPolicy, MessagePolicy responsePolicy, CallbackHandler handler,
        @SuppressWarnings("rawtypes") Map options) throws AuthException {
        this.handler = handler;
     	System.out.println("SAM -------  initialized");
    }

    @Override
    public AuthStatus validateRequest(MessageInfo messageInfo, Subject clientSubject, Subject serviceSubject)
        throws AuthException {

        HttpServletRequest request = (HttpServletRequest) messageInfo.getRequestMessage();
        HttpServletResponse response = (HttpServletResponse) messageInfo.getResponseMessage();
        System.out.println("login -------  triggered");

    	if (Jaspic.isAuthenticationRequest(request)) {
			return issueingPhase(clientSubject,this.handler,request, response);
		}

		if (Jaspic.isProtectedResource(messageInfo)) {
			return validationPhase(clientSubject,this.handler,request,response);
		}
        
 

    

        return SUCCESS;
    }

  	@Override
    public Class<?>[] getSupportedMessageTypes() {
        return supportedMessageTypes;
    }

    @Override
    public AuthStatus secureResponse(MessageInfo messageInfo, Subject serviceSubject) throws AuthException {
        return SEND_SUCCESS;
    }

    @Override
    public void cleanSubject(MessageInfo messageInfo, Subject subject) throws AuthException {

    }
    private AuthStatus validationPhase(Subject clientSubject, CallbackHandler handler2, HttpServletRequest request, HttpServletResponse response) {
		// Get the HTTP Authorization header from the request
		String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (authorizationHeader == null || authorizationHeader.isEmpty())

			return Jaspic.responseUnauthorized(response);

		// Extract the token from the HTTP Authorization header
		String token = authorizationHeader.substring("Bearer".length()).trim();

		try {

			// Validate the token
			Key key = generateKey();
			Jws<Claims> claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token);

			return Jaspic.notifyContainerAboutLogin(clientSubject, handler,claims.getBody().getSubject(),
					new HashSet<>(Arrays.asList(claims.getBody().getSubject())));

		} catch (Exception e) {
			e.printStackTrace();
			return Jaspic.responseUnauthorized(response);
		}
	}

	private AuthStatus issueingPhase(Subject clientSubject, CallbackHandler handler2, HttpServletRequest request, HttpServletResponse response) {

		
	
	 AuthParams params = Jaspic.getAuthParameters(request);
		AuthenticationStatus result = this.authn.validate(params);
		if (result == AuthenticationStatus.SUCCESS) {
			String token = issueTokenFor(params.getUser(), request);
			response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
			return Jaspic.notifyContainerAboutLogin(clientSubject, handler2, params.getUser(), this.authz.getCallerGroups(params));
		}

		return Jaspic.responseUnauthorized(response);

	}

	public static Key generateKey() {
		String keyString = "simplekey";
		Key key = new SecretKeySpec(keyString.getBytes(), 0, keyString.getBytes().length, "DES");
		return key;
	}

	private String issueTokenFor(String user, HttpServletRequest request) {

		Key key = generateKey();
		String jwt = Jwts.builder().setSubject(user).setIssuer(request.getLocalName().toString())
				.setIssuedAt(Date.from(Instant.now())).setId(UUID.randomUUID().toString())
				.setExpiration(Date.from(Instant.now().plusSeconds(120l))).signWith(SignatureAlgorithm.HS512, key)
				.compact();
		return jwt;
	}

}