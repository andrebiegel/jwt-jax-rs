package de.abiegel.jaxrs.auth;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 * 
 * @author abiegel
 *
 */
@Path("/hello")
@DeclareRoles({"user","admin"})
@Produces(MediaType.TEXT_PLAIN)
public class SecuredResource {

	@GET
	public String helloAnonymous(@QueryParam("message") String message) {
		return "hello " + message; 
	}
	@Inject javax.security.enterprise.SecurityContext context;
	
	@GET
	@Path("secured")
	
	@RolesAllowed({ "user" })
	public String helloPersonalized(@Context HttpServletRequest request, @QueryParam("message") String message) {
		return "hello " + request.getUserPrincipal().getName() + " " + message;
	}
}
