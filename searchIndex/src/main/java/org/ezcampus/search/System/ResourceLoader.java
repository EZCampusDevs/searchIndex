package org.ezcampus.search.System;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Map;

import org.tinylog.Logger;
import org.tinylog.configuration.Configuration;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class ResourceLoader
{
    public static void loadTinyLogConfig()
    {
        try
        {
            Configuration.set("writer1", "file");
            Configuration.set("writer1.file", Path.of(GlobalSettings.Log_Dir, GlobalSettings.Log_File).toString());
            Configuration.set("writer1.format", "[{date: yyyy-MM-dd HH:mm:ss.SSS}] [{level}] {message}");
            Configuration.set("writer1.append", "true");
            Configuration.set("writer1.level", "trace");

            Configuration.set("writer2", "console");
            Configuration.set("writer2.format", "[{date: yyyy-MM-dd HH:mm:ss.SSS}] [{level}] {message}");

            if(GlobalSettings.IS_DEBUG)
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
    
    
    public static void loadEnv() {
    	
    	GlobalSettings.Log_File =  new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss.log").format(new java.util.Date());
    	
    	String log_file = System.getenv("LOG_FILE");
    	String log_dir = System.getenv("LOG_DIR");
    	String username = System.getenv("DB_USER");
    	String password = System.getenv("DB_PASSWORD");
    	String db_port = System.getenv("DB_PORT");
    	String db_host = System.getenv("DB_HOST");
    	String db_name = System.getenv("DB_NAME");
    	String token = System.getenv("TOKEN_FILE");
    	String debug = System.getenv("IS_DEBUG");
    	
    	if(debug != null && !debug.isBlank()) {
    		GlobalSettings.IS_DEBUG = debug.toLowerCase().equals("true");
        	System.out.println(String.format("IS_DEBUG Loaded: %s", GlobalSettings.IS_DEBUG ));
    	}
    	
    	if(log_dir != null && !log_dir.isBlank()) {
    		GlobalSettings.Log_Dir = Path.of(log_dir).toString();
        	System.out.println(String.format("Log_Dir Loaded: %s", GlobalSettings.Log_Dir ));
    	}
    	
    	if(log_file != null && !log_file.isBlank()) {
    		GlobalSettings.Log_File = log_file;
        	System.out.println(String.format("Log_File Loaded: %s", GlobalSettings.Log_File ));
    	}
    	
    	if(username != null && !username.isBlank()) {
    		GlobalSettings.DB_User = username;
            System.out.println(String.format("MySQL User Loaded: %s", GlobalSettings.DB_User ));	
    	}
    	
    	if(password != null && !password.isBlank()) {
    		GlobalSettings.DB_Password = password;
            System.out.println("MySQL password: *******");	
    	}
    	
    	if(db_name != null && !db_name.isBlank()) {
    		GlobalSettings.DB_Name = db_name;
    		System.out.println(String.format("MySQL db name Loaded: %s", GlobalSettings.DB_Name ));
    	}
    	
    	if(db_host != null && !db_host.isBlank()) {
    		GlobalSettings.DB_Host = db_host;
        	System.out.println(String.format("MySQL host Loaded: %s", GlobalSettings.DB_Host ));
    	}
    	
    	if(db_port != null && !db_host.isBlank()) {
    		GlobalSettings.DB_Port = db_port;
        	System.out.println(String.format("MySQL port Loaded: %s", GlobalSettings.DB_Port ));
    	}
    	
    	if(token != null && !token.isBlank()) {
    		GlobalSettings.Token_File_Path = token;
    		System.out.println(String.format("Token file: %s", GlobalSettings.Token_File_Path ));
    	}
    }
    
    
    public static void loadToken() throws StreamReadException, DatabindException, IOException {
        
        final String TOKENS_PATH = GlobalSettings.Token_File_Path;
        
        if(!new File(TOKENS_PATH).isFile()) {
        	System.out.println(String.format("Token file %s does not exist", TOKENS_PATH));
        	return;
        }

        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, String> tokens = objectMapper.readValue(Paths.get(TOKENS_PATH).toFile(), Map.class);

        if(tokens.containsKey("DB_USER")) {
        	GlobalSettings.DB_User = (String)tokens.get("DB_USER");
            System.out.println(String.format("MySQL User Loaded: %s", GlobalSettings.DB_User ));	
        }
        
        if(tokens.containsKey("DB_PASSWORD")) {
        	GlobalSettings.DB_Password = (String)tokens.get("DB_PASSWORD");
            System.out.println("MySQL password: *******");	
        }
        
        if(tokens.containsKey("DB_PORT")) {
        	GlobalSettings.DB_Port = (String) tokens.get("DB_PORT");
        	System.out.println(String.format("MySQL port Loaded: %s", GlobalSettings.DB_Port ));
        }
        
        if(tokens.containsKey("DB_NAME")) {
        	GlobalSettings.DB_Name = (String)tokens.get("DB_NAME");	
        	System.out.println(String.format("MySQL db name Loaded: %s", GlobalSettings.DB_Name ));
        }
        
        if(tokens.containsKey("DB_HOST")) {
        	GlobalSettings.DB_Host = (String)tokens.get("DB_HOST");	
        	System.out.println(String.format("MySQL host Loaded: %s", GlobalSettings.DB_Host ));
        }
        
        if(tokens.containsKey("LOG_FILE")) {
        	GlobalSettings.Log_File = (String)tokens.get("LOG_FILE");	
        	System.out.println(String.format("Log_File Loaded: %s", GlobalSettings.Log_File ));
        }
        
        if(tokens.containsKey("LOG_DIR")) {
        	GlobalSettings.Log_Dir = (String)tokens.get("LOG_DIR");	
        	System.out.println(String.format("Log_Dir Loaded: %s", GlobalSettings.Log_Dir ));
        }
    }

}
