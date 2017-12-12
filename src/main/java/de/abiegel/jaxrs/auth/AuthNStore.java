package de.abiegel.jaxrs.auth;


import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;


public class AuthNStore {

	  private Map<String, String> callerToPassword;

	    @PostConstruct
	    public void init() {
	        callerToPassword = new HashMap<>();
	        callerToPassword.put("user", "42");
	        callerToPassword.put("admin", "42");
	        callerToPassword.put("god", "42");
	    }
	
	public AuthenticationStatus validate(AuthParams credential) {
		AuthenticationStatus result;
      
      
            String expectedPW = callerToPassword.get(credential.getUser());
            // We don't allow empty passwords :)
            if (expectedPW != null && expectedPW.equals(credential.getPasswort())) {
                result = AuthenticationStatus.SUCCESS;
        } else {
            result = AuthenticationStatus.NOT_DONE;
        }

        return result;
		
	}
}
