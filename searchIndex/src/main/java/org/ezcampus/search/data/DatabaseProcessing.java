package org.ezcampus.search.data;

import java.util.List;

import org.ezcampus.search.System.ResourceLoader;
import org.ezcampus.search.data.exceptions.ThreadShuttingDownException;
import org.ezcampus.search.data.threading.ThreadHandling;
import org.ezcampus.search.hibernate.entity.CourseData;
import org.ezcampus.search.hibernate.entity.CourseFaculty;
import org.ezcampus.search.hibernate.entity.Faculty;
import org.ezcampus.search.hibernate.entity.Meeting;
import org.ezcampus.search.hibernate.entity.ScrapeHistory;
import org.ezcampus.search.hibernate.entityDAO.ScrapeHistoryDAO;
import org.ezcampus.search.hibernate.entityDAO.WordDAO;
import org.ezcampus.search.hibernate.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.tinylog.Logger;

public class DatabaseProcessing
{
	public static final String WORD_INSERT_SPLIT_REGEX = "[\\s,_]";

	private static boolean _isProcessing = false;
	private static long _processingStartedAtNano = System.nanoTime();
	private static long _processingStartedAt = System.currentTimeMillis();
	private static long _processingEndedAtNano = System.nanoTime();
	private static long _processingEndedAt = System.currentTimeMillis();

	public static boolean isProcessing()
	{
		return _isProcessing;
	}

	public static long getProcessElapsedTime()
	{
		if (isProcessing())
		{
			return System.nanoTime() - _processingStartedAtNano;
		}

		return _processingEndedAtNano - _processingStartedAtNano;
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

	public static void main(String[] args) throws ThreadShuttingDownException
	{
		ResourceLoader.loadTinyLogConfig();
		processLastScrape();
	}

	public static void splitInsertWord(String value, CourseData courseData, Session session)
	{
		if (value == null)
			return;

		for (String v : value.split(WORD_INSERT_SPLIT_REGEX))
		{
			if (v == null || v.isBlank())
			{
				continue;
			}

			String number = StringHelper.getRomanNumeralMapping(v);

			if (number != null)
			{
				Logger.debug("Inserting word link for value '{}'", number);
				WordDAO.insertLinkWord(number, courseData, session);
			}

			Logger.debug("Inserting word link for value '{}'", v);
			WordDAO.insertLinkWord(v, courseData, session);
		}
	}

	public static void processDatabase() throws ThreadShuttingDownException
	{
		long start = System.nanoTime();
		
		List<ScrapeHistory> hist = ScrapeHistoryDAO.getUnindexedScrapeHistory();
		
		for(ScrapeHistory s : hist) 
		{
			if(s.getHasBeenIndexed())
				continue;
			
			_processScrape(s);
		}
		
		long stopms = (long) ((System.nanoTime() - start) / 1e6);
		Logger.info("Finished indexing after {}ms", stopms);
	}

	public static void processLastScrape() throws ThreadShuttingDownException
	{
		long start = System.nanoTime();

		_processLastScrape();

		long stopms = (long) ((System.nanoTime() - start) / 1e6);

		Logger.info("Finished indexing after {}ms", stopms);
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

		int i = 0;
		Transaction tx = null;

		try (Session session = HibernateUtil.getSessionFactory().openSession())
		{
			_isProcessing = true;
			_processingStartedAt = System.currentTimeMillis();
			_processingStartedAtNano = System.nanoTime();

			tx = session.getTransaction();

//			final String HQL1 = "SELECT cd, c, t FROM CourseData cd JOIN cd.course c JOIN c.term t";
			final String HQL1 = "FROM CourseData cd WHERE cd.scrapeId = :sid";
			List<CourseData> courseData_Course_Term = session.createQuery(HQL1, CourseData.class)
					.setParameter("sid", scrape).getResultList();

			int iteration = 0;

			Logger.info("Found {} course terms", courseData_Course_Term.size());

			for (CourseData courseData : courseData_Course_Term)
			{
				ThreadHandling.dieIfShuttingDown();
				Logger.debug("At CourseData iteration: {}", iteration);

				iteration++;

				final String HQL2 = "FROM CourseFaculty cf JOIN FETCH cf.faculty WHERE cf.courseDataId = :cId";
				List<CourseFaculty> courseFaculty_List = session.createQuery(HQL2, CourseFaculty.class)
						.setParameter("cId", courseData).getResultList();

				// insert all instructor names
				for (CourseFaculty courseFaculty : courseFaculty_List)
				{
					Faculty faculty = courseFaculty.getFaculty();
					splitInsertWord(faculty.getInstructorName(), courseData, session);

				}

				final String HQL3 = "FROM Meeting m WHERE m.courseDataId = :cId";
				List<Meeting> meetings = session.createQuery(HQL3, Meeting.class).setParameter("cId", courseData)
						.getResultList();

				// Put all unique search strings into searchWords
				for (Meeting meeting : meetings)
				{
					splitInsertWord(meeting.getBuildingDescription(), courseData, session);
				}

				splitInsertWord(courseData.getCourseTitle(), courseData, session);
				splitInsertWord(courseData.getSubjectLong(), courseData, session);

				splitInsertWord(courseData.getCrn(), courseData, session);
				splitInsertWord(courseData.getCourse().getCourseCode(), courseData, session); // Course Code, Math1010U

				splitInsertWord(courseData.getSubject(), courseData, session);
				splitInsertWord(courseData.getCampusDescription(), courseData, session);
				splitInsertWord(courseData.getInstructionalMethodDescription(), courseData, session);

				// TODO: Call more columns here to improve search results

			}

			if (tx.isActive())
			{
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
		finally
		{
			_processingEndedAt = System.currentTimeMillis();
			_processingEndedAtNano = System.nanoTime();
			_isProcessing = false;
		}

		Logger.info("Word & Word Map | Tables Loaded-in. Successfully Completed !");
	}

	public static void _processLastScrape() throws ThreadShuttingDownException
	{
		if (isProcessing())
		{
			Logger.warn("Cannot process the database while processing is in progress");
			return;
		}

		ScrapeHistory lastScrape = ScrapeHistoryDAO.getNewestScrapeHistory();

		if (lastScrape == null)
		{
			Logger.warn("Cannot index data because there is no known scrape history!");
			return;
		}

		Logger.info("Last found scrape time was: {}", lastScrape.toString());

		if (lastScrape.getHasBeenIndexed())
		{
			Logger.info("No data to process, last scrape time was already indexed.");
			return;
		}

		return;
	}
}
