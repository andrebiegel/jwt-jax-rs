package de.abiegel.jaxrs.auth;

import javax.security.auth.message.config.AuthConfigFactory;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class Configurator implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		
		  AuthConfigFactory factory = AuthConfigFactory.getFactory();
	        factory.registerConfigProvider(
	      new TokenAuthConfigProvider(), 
	      // layer 
	      "HttpServlet",
	      // app context --> for all apps on server
	      null, 
	      "The test"
	     );
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		
	}

}
