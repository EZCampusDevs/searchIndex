package org.ezcampus.search.hibernate.util;



import java.io.File;

import org.ezcampus.search.hibernate.entity.ClassType;
import org.ezcampus.search.hibernate.entity.Course;
import org.ezcampus.search.hibernate.entity.CourseData;
import org.ezcampus.search.hibernate.entity.CourseFaculty;
import org.ezcampus.search.hibernate.entity.Faculty;
import org.ezcampus.search.hibernate.entity.Meeting;
import org.ezcampus.search.hibernate.entity.ScrapeHistory;
import org.ezcampus.search.hibernate.entity.Term;
import org.ezcampus.search.hibernate.entity.Word;
import org.ezcampus.search.hibernate.entity.WordMap;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.tinylog.Logger;

public class HibernateUtil
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
				
				Configuration config = new Configuration();
				 
		        config.setProperty("hibernate.driver_class", "com.mysql.cj.jdbc.Driver");
		        config.setProperty("hibernate.url", "jdbc:mysql://localhost:3306/hibernate_db?useSSL=false&allowPublicKeyRetrieval=true");
		        config.setProperty("hibernate.username", "test");
		        config.setProperty("hibernate.password", "root");
		        config.setProperty("hibernate.pool_size", "1");
		        // config.setProperty("hibernate.show_sql", "true");
		        config.setProperty("hibernate.generate_statistics", "false");
		        
		        config.setProperty("hibernate.current_session_context_class", "thread");
		        config.setProperty("hibernate.hbm2ddl.auto", "update");
		        config.setProperty("hibernate.session.events.log.LOG_QUERIES_SLOWER_THAN_MS", "1");
		        
		        config.setProperty("hibernate.dbcp.initialSize", "5");
		        config.setProperty("hibernate.dbcp.maxTotal", "20");
		        config.setProperty("hibernate.dbcp.maxIdle", "10");
		        config.setProperty("hibernate.dbcp.minIdle", "5");
		        config.setProperty("hibernate.dbcp.maxWaitMillis", "-1");
		        
//		        config.setProperty("hibernate.jdbc.batch_size", "50");
//		        config.setProperty("hibernate.order_inserts", "true");
//		        config.setProperty("hibernate.order_updates", "true");
//		        config.setProperty("hibernate.batch_versioned_data", "true");
		 
		        config.addAnnotatedClass(Word.class);
		        config.addAnnotatedClass(WordMap.class);
		        config.addAnnotatedClass(Term.class);
		        config.addAnnotatedClass(ClassType.class);
		        config.addAnnotatedClass(Course.class);
		        config.addAnnotatedClass(CourseData.class);
		        config.addAnnotatedClass(Meeting.class);
		        config.addAnnotatedClass(Faculty.class);
		        config.addAnnotatedClass(CourseFaculty.class);
		        config.addAnnotatedClass(ScrapeHistory.class);
		        
				sessionFactory = config.buildSessionFactory();
				
/*
				// Create registry
				registry = new StandardServiceRegistryBuilder().configure(CONFIG_PATH).build();

				// Create MetadataSources
				MetadataSources sources = new MetadataSources(registry);

				// Create Metadata
				Metadata metadata = sources.getMetadataBuilder().build();

				// Create SessionFactory
				sessionFactory = metadata.getSessionFactoryBuilder().build();
				
				*/

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
