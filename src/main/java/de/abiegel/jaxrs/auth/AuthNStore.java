package de.abiegel.jaxrs.auth;

import static javax.security.enterprise.identitystore.CredentialValidationResult.INVALID_RESULT;
import static javax.security.enterprise.identitystore.CredentialValidationResult.NOT_VALIDATED_RESULT;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;


@ApplicationScoped
public class AuthNStore implements IdentityStore {

	  private Map<String, String> callerToPassword;

	    @PostConstruct
	    public void init() {
	        callerToPassword = new HashMap<>();
	        callerToPassword.put("user", "42");
	        callerToPassword.put("admin", "42");
	        callerToPassword.put("god", "42");
	    }
	@Override
	public CredentialValidationResult validate(Credential credential) {
		CredentialValidationResult result;
        if (credential instanceof UsernamePasswordCredential) {
            UsernamePasswordCredential usernamePassword = (UsernamePasswordCredential) credential;
            String expectedPW = callerToPassword.get(usernamePassword.getCaller());
            // We don't allow empty passwords :)
            if (expectedPW != null && expectedPW.equals(usernamePassword.getPasswordAsString())) {
                result = new CredentialValidationResult(usernamePassword.getCaller());
            } else {
                result = INVALID_RESULT;
            }
        } else {
            result = NOT_VALIDATED_RESULT;
        }

        return result;
		
	}

	  @Override
	    public Set<ValidationType> validationTypes() {
	        return new HashSet<>(Arrays.asList(IdentityStore.ValidationType.VALIDATE));
	    }
}
