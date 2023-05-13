package org.ezcampus.search.rest;

import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;

import org.ezcampus.search.System.GlobalSettings;
import org.ezcampus.search.System.ResourceLoader;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.tinylog.Logger;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;

// MAIN ENTRY TO JAVA API PROGRAM

@ApplicationPath("/")
public class Program extends ResourceConfig {

	private static class TinylogHandler implements UncaughtExceptionHandler 
    {
        @Override
        public void uncaughtException(Thread thread, Throwable ex) 
        {
            Logger.error("! === Unhandled Exception === !");
            Logger.error(ex);
        }
    }
	
    public Program() {
        GlobalSettings.IS_DEBUG = true;

        ResourceLoader.loadTinyLogConfig();

        Logger.info("{} starting...", GlobalSettings.BRAND_LONG);
        Logger.info("Running as debug: {}", GlobalSettings.IS_DEBUG);

        // Set the package where the resources are located
        packages("org.ezcampus.search.rest");

        // Enable CORS
        register(CORSFilter.class);

    }
    
    @Provider
    public static class CORSFilter implements ContainerResponseFilter {
        @Override
        public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
                throws IOException {
        	
        	//Please note that setting Access-Control-Allow-Origin to "*" allows requests from any origin.
            responseContext.getHeaders().add("Access-Control-Allow-Origin", "*");
            
            responseContext.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
            responseContext.getHeaders().add("Access-Control-Allow-Headers", "Content-Type");
        }
    }

}
