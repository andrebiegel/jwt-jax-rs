package de.abiegel.jaxrs.auth;

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
@Produces(MediaType.TEXT_PLAIN)
public class SecuredResource {

	@GET
	public String helloAnonymous(@QueryParam("message") String message) {
		return "hello " + message; 
	}
	
	@GET
	@Path("secured")	
	
	public String helloPersonalized(@Context HttpServletRequest request, @QueryParam("message") String message) {
		return "hello " + request.getUserPrincipal().getName() + " " + message;
	}
}
