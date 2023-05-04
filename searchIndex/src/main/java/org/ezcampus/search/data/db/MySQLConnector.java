package org.ezcampus.search.data.db;

public class MySQLConnector extends DatabaseConnector
{
	public MySQLConnector()
	{
		super("mysql", "localhost", 3306, "mysql", "root");
	}
	
	public void checkJDBCDriver()
	{
		try
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
}
