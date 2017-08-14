package de.abiegel.jaxrs.auth;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("app")
public class App extends Application {

	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> res = new HashSet<>();
		res.add(LoginResource.class);
		res.add(SecuredResource.class);
		res.add(JwtSecuredFilter.class);
		
		
		return res; 
	}

}
