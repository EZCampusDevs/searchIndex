package org.ezcampus.search.data;

import java.util.List;

import org.ezcampus.search.data.exceptions.ThreadShuttingDownException;
import org.ezcampus.search.data.inserter.HashMapWordInserter;
import org.ezcampus.search.data.inserter.WordInserter;
import org.ezcampus.search.data.threading.ThreadHandling;
import org.ezcampus.search.hibernate.entity.CourseData;
import org.ezcampus.search.hibernate.entity.CourseFaculty;
import org.ezcampus.search.hibernate.entity.Meeting;
import org.ezcampus.search.hibernate.entity.ScrapeHistory;
import org.ezcampus.search.hibernate.entityDAO.ScrapeHistoryDAO;
import org.ezcampus.search.hibernate.entityDAO.WordMapDAO;
import org.ezcampus.search.hibernate.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.tinylog.Logger;

public class DatabaseProcessing
{
	public static final String WORD_INSERT_SPLIT_REGEX = "[\\s,]";

	private static boolean _isProcessing = false;
	private static long _processingStartedAtNano = System.nanoTime();
	private static long _processingStartedAt = System.currentTimeMillis();
	private static long _processingEndedAtNano = System.nanoTime();
	private static long _processingEndedAt = System.currentTimeMillis();
	
	private static WordInserter inserter = new HashMapWordInserter();

	public static boolean isProcessing()
	{
		return _isProcessing;
	}

	public static long getProcessElapsedTimeMS()
	{
		if (isProcessing())
		{
			return (long)((System.nanoTime() - _processingStartedAtNano) / 1e6);
		}

		return (long)((_processingEndedAtNano - _processingStartedAtNano) / 1e6);
	}

	public static long getStartTimeNano()
	{
		return _processingStartedAtNano;
	}

	public static long getStartTime()
	{
		return _processingStartedAt;
	}

	public static long getEndTime()
	{
		return _processingEndedAt;
	}


	public static void processDatabase() throws ThreadShuttingDownException
	{
		Logger.info("Processing all scraped data");
		
		_isProcessing = true;
		_processingStartedAt = System.currentTimeMillis();
		_processingStartedAtNano = System.nanoTime();
		
		try 
		{
			for(ScrapeHistory s : ScrapeHistoryDAO.getUnindexedScrapeHistory()) 
			{
				if(s.getHasBeenIndexed() || !s.hasFinishedScraping)
					continue;
				
				_processScrape(s);
			}
		}
		finally 
		{
			_processingEndedAt = System.currentTimeMillis();
			_processingEndedAtNano = System.nanoTime();
			_isProcessing = false;
		}
		
		
		Logger.info("Finished indexing after {}ms", getProcessElapsedTimeMS());
	}



	public static void _processScrape(ScrapeHistory scrape) throws ThreadShuttingDownException
	{
		if (scrape == null)
		{
			Logger.warn("Cannot index data because there is no known scrape history!");
			return;
		}

		Logger.info("About to process scrape: {}", scrape.toString());

		if (scrape.getHasBeenIndexed())
		{
			Logger.info("The scrape has already been indexed.");
			return;
		}

		ThreadHandling.dieIfShuttingDown();


		Transaction tx = null;

		try (Session session = HibernateUtil.getSessionFactory().openSession())
		{
			int iteration = 0;

			tx = session.getTransaction();
			
			inserter.init(session);

			final String HQL1 = "FROM CourseData cd WHERE cd.scrapeId = :sid AND cd.shouldBeIndexed = :sbi";
			List<CourseData> courseDatas = session.createQuery(HQL1, CourseData.class)
												.setParameter("sid", scrape)
												.setParameter("sbi", true)
												.getResultList();


			for (CourseData courseData : courseDatas)
			{
				ThreadHandling.dieIfShuttingDown();
				
				++iteration;
				
				Logger.info("Processing iteration: {} / {}", iteration, courseDatas.size());
				
				WordMapDAO.removeMap(session, courseData);

				final String HQL2 = "FROM CourseFaculty cf JOIN FETCH cf.faculty WHERE cf.courseDataId = :cId";
				session.createQuery(HQL2, CourseFaculty.class)
					   .setParameter("cId", courseData)
					   .getResultList()
					   .forEach(x -> splitInsertWord(x.getFaculty().getInstructorName(), courseData, session));
				
				
				final String HQL3 = "FROM Meeting m WHERE m.courseDataId = :cId";
				session.createQuery(HQL3, Meeting.class)
					   .setParameter("cId", courseData)
					   .getResultList()
					   .forEach(x -> splitInsertWord(x.getBuildingDescription(), courseData, session));

				splitInsertWord(courseData.getCourseTitle(), courseData, session);
				
				splitInsertWord(courseData.getSubject().getSubject(), courseData, session);
				splitInsertWord(courseData.getSubject().getSubjectLong(), courseData, session);

				splitInsertWord(courseData.getCrn(), courseData, session);
				splitInsertWord(courseData.getCourse().getCourseCode(), courseData, session);

				splitInsertWord(courseData.getCampusDescription(), courseData, session);
				splitInsertWord(courseData.getDelivery(), courseData, session);
				
				inserter.associateWords(session, courseData);
				
				courseData.setShouldBeIndexed(false);
				
				if(!tx.isActive())
					tx.begin();
				
				session.persist(courseData);
				
				if(tx.isActive())
					tx.commit();
			}

			ScrapeHistoryDAO.setHasBeenIndexedById(scrape.getScrapeId(), true);
		}
		catch (HibernateException e)
		{
			Logger.error(e);

			if (tx != null && !tx.isActive())
			{
				tx.rollback();
			}
			throw e;
		}
	}
	

	public static void splitInsertWord(String value, CourseData courseData, Session session)
	{
		if (value == null)
			return;

		for (String v : value.split(WORD_INSERT_SPLIT_REGEX))
		{
			v = StringHelper.cleanWord(v);
			
			inserter.insertWord(session, StringHelper.getNumberMapping(v));
			inserter.insertWord(session, StringHelper.getRomanNumeralMapping(v));
			inserter.insertWord(session, StringHelper.getSpecialCharacterMapping(v));
			inserter.insertWord(session, v);
		}
	}
}
