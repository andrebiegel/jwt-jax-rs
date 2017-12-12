package de.abiegel.jaxrs.auth;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.HttpURLConnection;
import java.net.URI;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Test;



public class JwtSecuredIT {

	@Test
	public void testUnSecuredResourceAccess() {
		
		 WebTarget target = ClientBuilder.newClient().target(URI.create("http://localhost:"+System.getenv("it-backend.port")+"/jaxrs-auth-example-0.0.1-SNAPSHOT/app"));
		 
		String result = target.path("/hello").queryParam("message", "dude").request(MediaType.TEXT_PLAIN).get(String.class);
		 assertEquals("hello dude", result);
	}

	@Test(expected= NotAuthorizedException.class)
	public void testSecuredResourceAccess() {
		 WebTarget target = ClientBuilder.newClient().target(URI.create("http://localhost:"+System.getenv("it-backend.port")+"/jaxrs-auth-example-0.0.1-SNAPSHOT/app/hello"));
		 target.path("/secured").queryParam("message", "dude").request(MediaType.TEXT_PLAIN).get(String.class);
		
	}

	@Test
	public void testLogin() {
		System.out.println("http://localhost:"+System.getenv("it-backend.port")+"/jaxrs-auth-example-0.0.1-SNAPSHOT/app");
		
		 WebTarget target = ClientBuilder.newClient().target(URI.create("http://localhost:"+System.getenv("it-backend.port")+"/jaxrs-auth-example-0.0.1-SNAPSHOT/app"));
		 Form form = new Form();
		 form.param("user", "user");
		 form.param("password", "42");
		 System.out.println(Entity.form(form).toString());
		 Response response = target.path("/login").request(MediaType.APPLICATION_JSON).post(Entity.form(form), Response.class);
		 assertEquals(HttpURLConnection.HTTP_NO_CONTENT,response.getStatus());
		 assertNotNull(response.getHeaderString(HttpHeaders.AUTHORIZATION));
		 
		
	}
	
	@Test
	public void testSecuredAccessWithLogin() {
		 WebTarget target = ClientBuilder.newClient().target(URI.create("http://localhost:"+System.getenv("it-backend.port")+"/jaxrs-auth-example-0.0.1-SNAPSHOT/app"));
		 Form form = new Form();
		 form.param("user", "user");
		 form.param("password", "42");
		 System.out.println(Entity.form(form).toString());
		 Response response = target.path("/login").request(MediaType.APPLICATION_JSON).post(Entity.form(form), Response.class);
		 assertEquals(HttpURLConnection.HTTP_NO_CONTENT,response.getStatus());
		 assertNotNull(response.getHeaderString(HttpHeaders.AUTHORIZATION));
		 System.out.println(response.getHeaderString(HttpHeaders.AUTHORIZATION));
		 String authToken = response.getHeaderString(HttpHeaders.AUTHORIZATION);
		 WebTarget hello = ClientBuilder.newClient().target(URI.create("http://localhost:"+System.getenv("it-backend.port")+"/jaxrs-auth-example-0.0.1-SNAPSHOT/app/hello"));
		String result =hello.path("/secured").queryParam("message", "dude").request(MediaType.TEXT_PLAIN).header(HttpHeaders.AUTHORIZATION, authToken).get(String.class);
		 assertEquals("hello user dude", result);
	}
	
}
