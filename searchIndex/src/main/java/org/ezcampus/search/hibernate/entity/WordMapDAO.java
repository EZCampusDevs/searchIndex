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
	
	
	public static void insertMap2(Word word, CourseData courseData, Session session) 
	{
		Query<WordMap> query = session.createNamedQuery("insertWordMap", WordMap.class);
		query.setParameter("word", word);
		query.setParameter("courseData", courseData);
		query.setParameter("count", 1); 
		query.executeUpdate();
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
