package de.abiegel.jaxrs.auth;

import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.MessageInfo;
import javax.security.auth.message.config.ServerAuthConfig;
import javax.security.auth.message.config.ServerAuthContext;

public class TokenServerAuthConfig implements ServerAuthConfig {

	public TokenServerAuthConfig(String layer, String appContext, Object object,
			Map<String, String> providerProperties) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getMessageLayer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAppContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAuthContextID(MessageInfo messageInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isProtected() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ServerAuthContext getAuthContext(String authContextID, Subject serviceSubject, Map properties)
			throws AuthException {
		// TODO Auto-generated method stub
		return null;
	}

}
