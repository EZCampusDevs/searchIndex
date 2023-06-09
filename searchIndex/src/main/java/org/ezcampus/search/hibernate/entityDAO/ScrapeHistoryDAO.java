package org.ezcampus.search.hibernate.entityDAO;

import java.util.List;

import org.ezcampus.search.hibernate.entity.ScrapeHistory;
import org.ezcampus.search.hibernate.util.HibernateUtil;
import org.hibernate.Session;
import org.tinylog.Logger;

public class ScrapeHistoryDAO
{
	public static List<ScrapeHistory> getUnindexedScrapeHistory()
	{
		try (Session session = HibernateUtil.getSessionFactory().openSession())
		{
			return session
					.createQuery("SELECT s FROM ScrapeHistory s ORDER BY s.scrapeTime ASC", ScrapeHistory.class)
					.getResultList();
		}
	}
	
	public static ScrapeHistory getNewestScrapeHistory()
	{
		try (Session session = HibernateUtil.getSessionFactory().openSession())
		{
			return session
					.createQuery("SELECT s FROM ScrapeHistory s ORDER BY s.scrapeTime DESC", ScrapeHistory.class)
					.setMaxResults(1)
					.getSingleResultOrNull();
		}
	}

	
	public static void setHasBeenIndexedById(int scrapeId, boolean hasBeenIndex) 
	{
		 try (Session session = HibernateUtil.getSessionFactory().openSession()) {
		        ScrapeHistory scrapeHistory = session.get(ScrapeHistory.class, scrapeId);
		        if (scrapeHistory != null) {
		            scrapeHistory.setHasBeenIndexed(hasBeenIndex);
		            session.beginTransaction();
		            session.persist(scrapeHistory);
		            session.getTransaction().commit();
		        }
		    } catch (Exception e) {
		    	Logger.error(e, "Error while updating scrapeHistory with id {}", scrapeId);
		        e.printStackTrace();
		    }
	
	}
}
