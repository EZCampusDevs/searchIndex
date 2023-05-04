package org.ezcampus.search.data.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import org.ezcampus.search.System.GlobalSettings;
import org.ezcampus.search.System.ResourceLoader;
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
        connector.databaseName = "hibernate_db";
        connector.username = "test";
        connector.password = "root";
        
        Connection c = connector.getConnection("hibernate_db");
        
//        createNewDatabase();
//        createTables(true);
        
    }
}
