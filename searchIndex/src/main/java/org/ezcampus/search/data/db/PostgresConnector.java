package org.ezcampus.search.data.db;

import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgresConnector extends DatabaseConnector
{
	// pgdmin and postgres setup docker help
	// https://stackoverflow.com/questions/25540711/docker-postgres-pgadmin-local-connection

	// actually using postgres from java
	// https://www.postgresqltutorial.com/postgresql-jdbc/query/

	public PostgresConnector()
	{
		super("postgres", "localhost", 5432, "postgres", "123");
	}

	public void checkJDBCDriver()
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
}