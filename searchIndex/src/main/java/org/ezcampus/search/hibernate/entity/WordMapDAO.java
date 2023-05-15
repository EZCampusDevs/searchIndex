package org.ezcampus.search.hibernate.entity;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.tinylog.Logger;

public class WordMapDAO
{

	public static void insertMap(int wordId, int courseDataId, Session session) 
	{
		Word w = new Word();
		w.setId(wordId);
		
		CourseData cd = new CourseData();
		cd.setCourseDataId(courseDataId);
		
		insertMap(w, cd, session);
	}

	/**
	 * Inserts the given mapping, assumes you are already in a transaction
	 * @param word The word_id 
	 * @param courseData The course_data_id
	 * @param session The active database session
	 */
	public static void insertMap_NT(Word word, CourseData courseData, Session session) 
	{
		WordMap map = session.createQuery("FROM WordMap m "
				+ "JOIN FETCH m.word w "
				+ "JOIN FETCH m.courseData cd "
				+ "WHERE w = :word AND cd = :cd"
				, WordMap.class)
				.setParameter("word", word)
				.setParameter("cd", courseData)
				.setMaxResults(1)
				.getSingleResultOrNull();
		
		if(map != null)
		{
			map.increaseCountBy(1);
		}
		else {
			map = new WordMap(word, courseData, 1);
		}

		session.persist(map);
	}
	
	public static void insertMap(Word word, CourseData courseData, Session session) 
	{
		Transaction tx = null;
		try
		{
			WordMap map = session.createQuery("FROM WordMap m WHERE m.word = :word AND m.courseData = :cd", WordMap.class)
					.setParameter("word", word)
					.setParameter("cd", courseData)
					.setMaxResults(1)
					.getSingleResultOrNull();
			
			WordMap update ;
			
			if(map != null)
			{
				map.increaseCountBy(1);
				
				update = map;
			}
			else {
				update = new WordMap(word, courseData, 1);
			}
			
			tx = session.beginTransaction();

			session.persist(update);
			
			tx.commit();
		}
		catch (HibernateException e)
		{
			Logger.error(e);
			
			if (tx != null && tx.isActive())
			{
				tx.rollback();
			}
			throw e;
		}
	}
}
