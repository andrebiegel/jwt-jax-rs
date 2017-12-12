package de.abiegel.jaxrs.auth.base;


import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import de.abiegel.jaxrs.auth.Jaspic;



/**
 * 
 * @author Arjan Tijms
 * 
 */
@WebListener
public class SamAutoRegistrationListener implements ServletContextListener {

    @Override
	public void contextDestroyed(ServletContextEvent sce) {
     	System.out.println("SAM -------  Registration destroyed");
    	Jaspic.deregisterServerAuthModule(sce.getServletContext());
	}

	@Override
    public void contextInitialized(ServletContextEvent sce) {
    	System.out.println("SAM -------  Registration initiated");
        Jaspic.registerServerAuthModule(new JwtSam(), sce.getServletContext());
    }

}