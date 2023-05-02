package org.ezcampus.searchIndex.data.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.ezcampus.searchIndex.System.GlobalSettings;
import org.ezcampus.searchIndex.System.ResourceLoader;
import org.tinylog.Logger;

public class DatabaseConnection
{
	// pgdmin and postgres setup docker help
	// https://stackoverflow.com/questions/25540711/docker-postgres-pgadmin-local-connection

	// actually using postgres from java
	// https://www.postgresqltutorial.com/postgresql-jdbc/query/

	// public static final String POSTGRES_DOMAIN = "192.168.1.148";
	public static final String POSTGRES_DOMAIN = "localhost";
	public static final int POSTGRES_PORT = 5432;
	public static final String POSTGRES_URI = String.format("jdbc:postgresql://%s:%d/", POSTGRES_DOMAIN, POSTGRES_PORT);
	public static final String POSTGRES_USER = "postgres";
	// a very good idea, nobody will guess this ;3c
	public static final String POSTGRES_PASSWORD = "123";

	public static final String POSTGRES_DATABASE = "master";

	static
	{
		try
		{
			DriverManager.registerDriver(new org.postgresql.Driver());
		}
		catch (SQLException e)
		{
			throw new RuntimeException(e);
		}
	}

	/**
	 * Connect to the PostgreSQL database
	 *
	 * @return a Connection object
	 * @throws java.sql.SQLException
	 */
	public static Connection getConnection(String database) throws SQLException
	{
		return DriverManager.getConnection(POSTGRES_URI + database, POSTGRES_USER, POSTGRES_PASSWORD);
	}

	/**
	 * Connect to the PostgreSQL database
	 *
	 * @return a Connection object
	 * @throws java.sql.SQLException
	 */
	public static Connection getConnection() throws SQLException
	{
		return getConnection(POSTGRES_DATABASE);
	}

	/**
	 * Creates the database
	 */
	public static void createDatabase()
	{
		try (Connection c = getConnection(""); Statement statement = c.createStatement())
		{
			statement.executeUpdate("CREATE DATABASE master");
		}
		catch (SQLException e)
		{
			if (e.getMessage().equals("ERROR: database \"master\" already exists"))
			{
				return;
			}

			Logger.warn(e, "Error while creating a new database");
		}
	}

	/**
	 * Drops and creates the 'master' database
	 */
	public static void createNewDatabase()
	{
		try (Connection c = getConnection(""); Statement statement = c.createStatement())
		{
			statement.execute("DROP DATABASE IF EXISTS master");
			statement.executeUpdate("CREATE DATABASE master");
		}
		catch (SQLException e)
		{
			if (e.getMessage().equals("ERROR: database \"master\" already exists"))
			{
				return;
			}

			Logger.warn(e, "Error while creating a new database");
		}
	}

	/**
	 * Creates all the tables for the database
	 */
	public static void createTables()
	{
		createTables(false);
	}

	/**
	 * Creates all the tables for the database
	 * 
	 * @param fromNew Should all existing tables be dropped first
	 */
	public static void createTables(boolean fromNew)
	{
		try (Connection c = getConnection(); Statement statement = c.createStatement())
		{
			if (fromNew)
			{
				// NOTE: order matters!
				// TableHash is a primary key for a bunch of foreign keys
				// you must delete all foreign keys first before you can delete it
				statement.execute(TableTest.DELETION_QUERY);
			}

			// NOTE: order matters!
			// make sure foreign key tables are made last!!!
			statement.execute(TableTest.CREATION_QUERY);

		}
		catch (SQLException e)
		{
			Logger.warn(e, "Error while creating database tables");
		}
	}

	// DEBUG ONLY
	public static void main(String args[]) throws IOException, InterruptedException
    {
        ResourceLoader.loadTinyLogConfig();
        Logger.info("Starting...");

        GlobalSettings.IS_DEBUG = true;
        Logger.info("Running as debug: {}", GlobalSettings.IS_DEBUG);


        createNewDatabase();
        createTables(true);
        
    }
}