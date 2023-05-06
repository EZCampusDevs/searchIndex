package org.ezcampus.search.data.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import org.ezcampus.search.data.StringHelper;
import org.tinylog.Logger;

public abstract class DatabaseConnector
{
	protected final String JDBC_PROTOCOL;

	protected String domain = "localhost";

	protected int port;

	protected String username;

	protected String password;

	protected String databaseName = "master";

	protected HashMap<String, String> queryParams = new HashMap<>();

	protected DatabaseConnector(String JDBC_PROTOCOL)
	{
		this.JDBC_PROTOCOL = JDBC_PROTOCOL;
	}

	protected DatabaseConnector(String JDBC_PROTOCOL, String domain, int port)
	{
		this(JDBC_PROTOCOL);
		this.domain = domain;
		this.port = port;
	}

	protected DatabaseConnector(String JDBC_PROTOCOL, String domain, int port, String username, String password)
	{
		this(JDBC_PROTOCOL, domain, port);
		this.username = username;
		this.password = password;
	}

	public abstract void checkJDBCDriver();

	/**
	 * Gets the database connection URI
	 * 
	 * @return A string in the format jdbc:{JDBC_PROTOCOL}://{domain}:{port}
	 */
	public String getURI()
	{
		return String.format("jdbc:%s://%s:%d/", JDBC_PROTOCOL, domain, port);
	}
	
	public String getConnectionURI(String database)
	{
		if (queryParams.size() > 0)
			return 
					String.format("%s?%s", getURI() + database, StringHelper.urlEncodeUTF8(queryParams));

		return this.getURI() + database;
	}
	
	public String getConnectionURI()
	{
		return this.getConnectionURI(this.databaseName);
	}
	
	

	public void setURIPathQuery(String key, String value)
	{
		if (value == null)
		{
			if (queryParams.containsKey(key))
			{
				queryParams.remove(key);
			}
		}

		queryParams.put(key, value);
	}

	/**
	 * Connect to the database
	 *
	 * @return a Connection object
	 * @throws java.sql.SQLException
	 */
	public Connection getConnection() throws SQLException
	{
		return getConnection(this.databaseName);
	}

	/**
	 * Connect to the database
	 *
	 * @return a Connection object
	 * @throws java.sql.SQLException
	 */
	public Connection getConnection(String database) throws SQLException
	{
		return DriverManager.getConnection(this.getConnectionURI(database), username, password);
	}

	/**
	 * Creates the database
	 */
	public void createDatabase()
	{
		try (Connection c = this.getConnection(""); Statement statement = c.createStatement())
		{
			statement.executeUpdate(String.format("CREATE DATABASE %s", databaseName));
		}
		catch (SQLException e)
		{
			if (e.getMessage().equals(String.format("ERROR: database \"%s\" already exists", databaseName)))
			{
				return;
			}

			Logger.warn(e, "Error while creating a new database");
		}
	}
}
