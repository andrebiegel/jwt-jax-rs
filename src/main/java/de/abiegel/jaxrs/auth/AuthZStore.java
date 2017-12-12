package de.abiegel.jaxrs.auth;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class AuthZStore  {
	
	/**
	 * user name is the identity of the groups the user belongs to
	 */
    public Set<String> getCallerGroups(AuthParams user) {
        return new HashSet<>(Arrays.asList(user.getUser()));
    }

}
