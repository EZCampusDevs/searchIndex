package org.ezcampus.search.System;

import org.tinylog.Logger;
import org.tinylog.configuration.Configuration;

import java.nio.file.Path;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.file.Paths;
import java.util.Map;


public class ResourceLoader
{
    public static void loadTinyLogConfig()
    {
        try
        {
            Configuration.set("writer1", "file");
            Configuration.set("writer1.file", Path.of(GlobalSettings.LOGS_DIR_PATH.toString(), "logs.txt").toString());
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
    
    public static void loadToken() {
        
         Logger.info("dir {}", Paths.get(".").toAbsolutePath().toString());
        
        final String TOKENS_PATH = "./token.json";
        try {
            // Create ObjectMapper instance
            ObjectMapper objectMapper = new ObjectMapper();

            // Read the tokens.json file
            Map<?, ?> tokens = objectMapper.readValue(Paths.get(TOKENS_PATH).toFile(), Map.class);

            // Extract the SQL username and password
            GlobalSettings.MySQL_User = (String)tokens.get("username");
            Logger.info("MySQL User Loaded: {}",GlobalSettings.MySQL_User );
            
            GlobalSettings.MySQL_Password = (String)tokens.get("password");
            Logger.info("MySQL User Loaded: {}","******");

            GlobalSettings.Port = (int)tokens.get("port");
            Logger.info("Port: {}",GlobalSettings.Port );
            
            GlobalSettings.DB_Name = (String)tokens.get("db_name");
            Logger.info("DB Name: {}",GlobalSettings.DB_Name );
            
            GlobalSettings.Host = (String)tokens.get("host");
            Logger.info("DB Host: {}",GlobalSettings.Host );

        } catch (Exception e) {
            Logger.error(e);
        }
    }

}
