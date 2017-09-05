package de.abiegel.jaxrs.auth;

import static org.junit.Assert.*;

import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.junit.Test;

public class JwtSecuredIT {

	@Test
	public void testUnSecuredResourceAccess() {
		 WebTarget target = ClientBuilder.newClient().target(URI.create("http://localhost:8080/jax-rs-example/app"));
		 
		 
		 
		 target.path("/hello").queryParam("message", "dude");
		 
		 
	}

}
