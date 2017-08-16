package de.abiegel.jaxrs.auth;

import java.security.Key;

import java.time.Instant;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Path("login")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
@Transactional
public class LoginResource {
	
	 @Context
	 private UriInfo uriInfo;
	
	@POST
	public Response authenicate(@FormParam("user") String user, @FormParam("password") String password) {
	
		try {
			authenticateUser(user, password);
			String token = issueTokenFor(user);
			return Response.ok().header(HttpHeaders.AUTHORIZATION, "Bearer "+ token).build();
		} catch (Exception e) {
			return Response.status(Status.UNAUTHORIZED).build();
		}
	}


	private String issueTokenFor(String user) {
		
		
		Key key = generateKey();
		String jwt = Jwts.builder()
				.setSubject(user).setIssuer(uriInfo.getAbsolutePath().toString())
				.setIssuedAt(Date.from(Instant.now()))
				.setExpiration(Date.from(Instant.now().plusSeconds(15l)))
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
		return jwt;
	}


	private void authenticateUser(String user, String password) {
		if (!"42".equals(password)) {
			throw new SecurityException("no access bro");
		}
		
	}
	
    public static Key generateKey() {
        String keyString = "simplekey";
        Key key = new SecretKeySpec(keyString.getBytes(), 0, keyString.getBytes().length, "DES");
        return key;
    }
}
