package de.abiegel.jaxrs.auth;

import java.security.Key;
import java.util.Arrays;
import java.util.HashSet;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.enterprise.AuthenticationException;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import javax.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import javax.security.enterprise.identitystore.IdentityStoreHandler;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
	

@ApplicationScoped
public class TokenBasedAuth implements HttpAuthenticationMechanism {

	@Inject 
	IdentityStoreHandler handler; 
	@Override
	public AuthenticationStatus validateRequest(HttpServletRequest request, HttpServletResponse response,
			HttpMessageContext httpMessageContext) throws AuthenticationException {
		
		if (httpMessageContext.isProtected()) {
			 // Get the HTTP Authorization header from the request
	        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
	 
	        if (authorizationHeader == null || authorizationHeader.isEmpty()  )  
	        
	        	return httpMessageContext.responseUnauthorized();
	       
	        // Extract the token from the HTTP Authorization header
	        String token = authorizationHeader.substring("Bearer".length()).trim();
	 
	        try {
	 
	            // Validate the token
	            Key key = LoginResource.generateKey();
	          Jws<Claims> claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token);

	          
	         return  httpMessageContext.notifyContainerAboutLogin(claims.getBody().getSubject(), new HashSet<>(Arrays.asList(claims.getBody().getSubject())));
	           
	        } catch (Exception e) {
	        		System.out.println(e);
	        		return httpMessageContext.responseUnauthorized();
	        }

		}
		
	   return httpMessageContext.doNothing();
		
	}

}
