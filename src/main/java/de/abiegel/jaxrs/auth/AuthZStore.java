package de.abiegel.jaxrs.auth;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;


@ApplicationScoped
public class AuthZStore implements IdentityStore {
	
    @Override
    public Set<String> getCallerGroups(CredentialValidationResult validationResult) {
        return new HashSet<>(Arrays.asList(validationResult.getCallerPrincipal().getName()));
    }

    @Override
    public Set<ValidationType> validationTypes() {
        return new HashSet<>(Arrays.asList(IdentityStore.ValidationType.PROVIDE_GROUPS));
    }
}
