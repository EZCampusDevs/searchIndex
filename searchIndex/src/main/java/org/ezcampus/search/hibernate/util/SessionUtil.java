package org.ezcampus.search.hibernate.util;



import java.io.File;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.tinylog.Logger;

public class SessionUtil
{
	private static StandardServiceRegistry registry;
	private static SessionFactory sessionFactory;

	public static SessionFactory getSessionFactory()
	{
		final String CONFIG_PATH = "hibernate.cfg.xml";
		
		Logger.debug("Config path {} exists: {}", CONFIG_PATH, new File(CONFIG_PATH).exists());
		
		if (sessionFactory == null)
		{
			try
			{
				
//			    Configuration cfg = new Configuration().addResource("hibernate.cfg.xml")  ;
//			    Logger.debug("Config created? {}", cfg);
				// Create registry
				registry = new StandardServiceRegistryBuilder().configure(CONFIG_PATH).build();

				// Create MetadataSources
				MetadataSources sources = new MetadataSources(registry);

				// Create Metadata
				Metadata metadata = sources.getMetadataBuilder().build();

				// Create SessionFactory
				sessionFactory = metadata.getSessionFactoryBuilder().build();

			}
			catch (Exception e)
			{
				Logger.error(e);

				if (registry != null)
				{
					StandardServiceRegistryBuilder.destroy(registry);
				}
			}
		}
		return sessionFactory;
	}

	public static void shutdown()
	{
		if (registry != null)
		{
			StandardServiceRegistryBuilder.destroy(registry);
		}
	}
}