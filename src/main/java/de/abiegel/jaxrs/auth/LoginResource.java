package de.abiegel.jaxrs.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

@Path("login")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
@Transactional
public class LoginResource {

	@Context
	private UriInfo uriInfo;


	@POST
	public void authenicate(@Context HttpServletRequest request, @Context HttpServletResponse response,
			@FormParam("user") String user, @FormParam("password") String password) {

		authenticateUser(request, response, user, password);
	}

	private void authenticateUser(HttpServletRequest request, HttpServletResponse response, String user,
			String password) {
		
		boolean result = Jaspic.authenticate(request, response,  AuthParams.withCredentials(user, password));
		
		System.out.println("login -------  " + result);
	}

}
