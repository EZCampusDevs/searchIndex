package org.ezcampus.search.data.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.ezcampus.search.System.GlobalSettings;
import org.ezcampus.search.System.ResourceLoader;
import org.tinylog.Logger;

public class Database
{

	public static DatabaseConnector connector;
	
	public static Connection getConnection() throws SQLException 
	{
		init();
		Connection c = connector.getConnection();
		return c;
	}
	
	public static void init() 
	{
		if(connector != null)
			return;

		connector = new MySQLConnector();
		connector.checkJDBCDriver();
		connector.databaseName = "hibernate_db";
		connector.username = "test";
		connector.password = "root";
		connector.setURIPathQuery("useSSL", "false");
		Logger.info("Database Connection URL: {}", connector.getConnectionURI());
	}

	// DEBUG ONLY
	public static void main(String args[]) throws IOException, InterruptedException, SQLException
	{
		ResourceLoader.loadTinyLogConfig();
		Logger.info("Starting...");

		GlobalSettings.IS_DEBUG = true;
		Logger.info("Running as debug: {}", GlobalSettings.IS_DEBUG);

		init();
		
		Connection c = connector.getConnection();

		final String SQL = "SELECT * FROM tbl_word LIMIT 200";

		try ( PreparedStatement pstmt = c.prepareStatement(SQL))
		{
			pstmt.execute();

			ResultSet rs = pstmt.executeQuery();

			while (rs.next())
			{
				Logger.info(rs.getString(2));
			}
		}
		catch (SQLException e)
		{
			Logger.error(e);
		}

	}
}
