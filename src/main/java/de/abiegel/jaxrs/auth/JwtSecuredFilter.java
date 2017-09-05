/**
 * 
 */
package de.abiegel.jaxrs.auth;

import java.io.IOException;
import java.security.Key;
import java.security.Principal;
import java.util.List;

import javax.annotation.Priority;
import javax.crypto.KeyGenerator;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
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
 
        if (authorizationHeader == null || authorizationHeader.isEmpty()  )  {
        	requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        	return; 
        }
        // Extract the token from the HTTP Authorization header
        String token = authorizationHeader.substring("Bearer".length()).trim();
 
        try {
 
            // Validate the token
            Key key = LoginResource.generateKey();
          Jws<Claims> claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token);

            final SecurityContext currentSecurityContext = requestContext.getSecurityContext();
            requestContext.setSecurityContext(new SecurityContext() {

                @Override
                public Principal getUserPrincipal() {

                    return new Principal() {

                        @Override
                        public String getName() {
                            return claims.getBody().getSubject();
                        }
                    };
                }

                @Override
                public boolean isUserInRole(String role) {
                    return role.equals(claims.getBody().getSubject());
                }

                @Override
                public boolean isSecure() {
                    return currentSecurityContext.isSecure();
                }

                @Override
                public String getAuthenticationScheme() {
                    return "Bearer";
                }
            });
 
        } catch (Exception e) {
        		System.out.println(e);
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }

	}

}
