package de.abiegel.jaxrs.auth.base;

import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;

import de.abiegel.jaxrs.auth.TokenServerAuthModule;

/**
 * 
 * @author Arjan Tijms
 * 
 */
@WebListener
public class SamAutoRegistrationListener extends BaseServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        JaspicUtils.registerSAM(sce.getServletContext(), new TokenServerAuthModule());
    }

}