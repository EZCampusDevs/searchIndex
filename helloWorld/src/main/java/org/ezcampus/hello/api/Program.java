package org.ezcampus.hello.api;

import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.glassfish.jersey.server.ResourceConfig;
import org.tinylog.Logger;
import org.tinylog.configuration.Configuration;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;

// MAIN ENTRY TO JAVA API PROGRAM

@ApplicationPath("/")
public class Program extends ResourceConfig {
	
	public static final Path LOGS_DIR_PATH = Paths.get(".", "logs");
	public static boolean IS_DEBUG = false;
	
	private static class TinylogHandler implements UncaughtExceptionHandler 
    {
        @Override
        public void uncaughtException(Thread thread, Throwable ex) 
        {
            Logger.error("! === Unhandled Exception === !");
            Logger.error(ex);
        }
    }
	
	public static void loadTinyLogConfig()
    {
        try
        {
            Configuration.set("writer1", "file");
            Configuration.set("writer1.file", Path.of(LOGS_DIR_PATH.toString(), "logs.txt").toString());
            Configuration.set("writer1.format", "[{date: yyyy-MM-dd HH:mm:ss.SSS}] [{level}] {message}");
            Configuration.set("writer1.append", "true");
            Configuration.set("writer1.level", "trace");

            Configuration.set("writer2", "console");
            Configuration.set("writer2.format", "[{date: yyyy-MM-dd HH:mm:ss.SSS}] [{level}] {message}");

            if(IS_DEBUG)
            {
                Configuration.set("writer2.level", "trace");
            }
            else
            {
                Configuration.set("writer2.level", "info");
            }

            Logger.info("TinyLog has initialized!");
        }
        catch (UnsupportedOperationException e)
        {
            Logger.warn("Tried to update tinylog config, it was already set");
        }
    }
	
    public Program() 
    {
    	loadTinyLogConfig();
        Thread.setDefaultUncaughtExceptionHandler(new TinylogHandler());
        
        Logger.info("{} starting...", "helloWorld");
        Logger.info("Running as debug: {}", IS_DEBUG);

        // Set the package where the resources are located
        packages("org.ezcampus.hello.api");

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
