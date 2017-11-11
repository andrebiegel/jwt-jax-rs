package de.abiegel.jaxrs.auth;

import java.io.IOException;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.AuthStatus;
import javax.security.auth.message.MessageInfo;
import javax.security.auth.message.MessagePolicy;
import javax.security.auth.message.callback.CallerPrincipalCallback;
import javax.security.auth.message.callback.GroupPrincipalCallback;
import javax.security.auth.message.module.ServerAuthModule;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TokenServerAuthModule implements ServerAuthModule {

	// Key in the MessageInfo Map that when present AND set to true indicated a protected resource is being accessed.
		// When the resource is not protected, GlassFish omits the key altogether. WebSphere does insert the key and sets
		// it to false.
		private static final String IS_MANDATORY = "javax.security.auth.message.MessagePolicy.isMandatory";
	  private CallbackHandler handler;
	    private Class<?>[] supportedMessageTypes = 
	        new Class[] {HttpServletRequest.class, HttpServletResponse.class };
	    
	    public static boolean isProtectedResource(MessageInfo messageInfo) {
			return Boolean.valueOf((String) messageInfo.getMap().get(IS_MANDATORY));
		}   
	    
	    
	@Override
	public AuthStatus validateRequest(MessageInfo messageInfo, Subject clientSubject, Subject serviceSubject)
			throws AuthException {
	    // Normally we would check here for authentication credentials being
        // present and perform actual authentication, or in absence of those
        // ask the user in some way to authenticate.
 
		
        // Here we just create the user and associated roles directly.
 
        // Create a handler (kind of directive) to add the caller principal (AKA
        // user principal) "test" (=basically user name, or user id)
        // This will be the name of the principal returned by e.g.
        // HttpServletRequest#getUserPrincipal
        CallerPrincipalCallback callerPrincipalCallback = 
            new CallerPrincipalCallback(clientSubject, "test");
 
        // Create a handler to add the group (AKA role) "architect"
        // This is what e.g. HttpServletRequest#isUserInRole and @RolesAllowed
        // test for
        GroupPrincipalCallback groupPrincipalCallback = 
            new GroupPrincipalCallback(
                clientSubject, new String[] { "architect" }
            );
 
        // Execute the handlers we created above. This will typically add the
        // "test" principal and the "architect"
        // role in an application server specific way to the JAAS Subject.
        try {
            handler.handle(new Callback[] { callerPrincipalCallback,
                    groupPrincipalCallback });
        } catch (IOException | UnsupportedCallbackException e) {
            e.printStackTrace();
        }
 
        return AuthStatus.SUCCESS;
	}

	   /**
     * WebLogic 12c calls this before Servlet is called, Geronimo v3 after,
     * JBoss EAP 6 and GlassFish 3.1.2.2 don't call this at all. WebLogic
     * (seemingly) only continues if SEND_SUCCESS is returned, Geronimo
     * completely ignores return value.
     */
    @Override
    public AuthStatus secureResponse(MessageInfo messageInfo,
            Subject serviceSubject) throws AuthException {
        return AuthStatus.SEND_SUCCESS;
    }

	@Override
	public void cleanSubject(MessageInfo messageInfo, Subject subject) throws AuthException {
		// TODO Auto-generated method stub

	}

	@Override
	public void initialize(MessagePolicy requestPolicy, MessagePolicy responsePolicy, CallbackHandler handler,
			Map options) throws AuthException {
		// TODO Auto-generated method stub
		this.handler = handler;
	}

	/**
     * A compliant implementation should return HttpServletRequest and
     * HttpServletResponse, so the delegation class {@link ServerAuthContext}
     * can choose the right SAM to delegate to. In this example there is only
     * one SAM and thus the return value actually doesn't matter here.
     */
    @Override
    public Class<?>[] getSupportedMessageTypes() {
        return supportedMessageTypes;
    }

}
