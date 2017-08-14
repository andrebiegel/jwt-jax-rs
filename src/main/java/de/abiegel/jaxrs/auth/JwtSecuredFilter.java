/**
 * 
 */
package de.abiegel.jaxrs.auth;

import java.io.IOException;
import java.security.Key;

import javax.annotation.Priority;
import javax.crypto.KeyGenerator;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import io.jsonwebtoken.Jwts;

/**
 * @author abiegel
 *
 */
@Provider
@JwtSecured
@Priority(Priorities.AUTHENTICATION)
public class JwtSecuredFilter implements ContainerRequestFilter {
	 
	
	/* (non-Javadoc)
	 * @see javax.ws.rs.container.ContainerRequestFilter#filter(javax.ws.rs.container.ContainerRequestContext)
	 */
	public void filter(ContainerRequestContext requestContext) throws IOException {
	    // Get the HTTP Authorization header from the request
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
 
        // Extract the token from the HTTP Authorization header
        String token = authorizationHeader.substring("Bearer".length()).trim();
 
        try {
 
            // Validate the token
            Key key = LoginResource.generateKey();
            Jwts.parser().setSigningKey(key).parseClaimsJws(token);

 
        } catch (Exception e) {

            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }

	}

}
