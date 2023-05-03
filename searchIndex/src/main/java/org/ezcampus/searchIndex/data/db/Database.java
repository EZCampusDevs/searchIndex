package org.ezcampus.searchIndex.data.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import org.ezcampus.searchIndex.System.GlobalSettings;
import org.ezcampus.searchIndex.System.ResourceLoader;
import org.tinylog.Logger;

public class Database
{

	public static DatabaseConnector connector;
	

	// DEBUG ONLY
	public static void main(String args[]) throws IOException, InterruptedException, SQLException
    {
        ResourceLoader.loadTinyLogConfig();
        Logger.info("Starting...");

        GlobalSettings.IS_DEBUG = true;
        Logger.info("Running as debug: {}", GlobalSettings.IS_DEBUG);


        connector = new MySQLConnector();
        connector.checkJDBCDriver();
        connector.username = "root";
        connector.password = "root";
        
        Connection c = connector.getConnection("mysql");
        
//        createNewDatabase();
//        createTables(true);
        
    }
}
