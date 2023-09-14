package org.ezcampus.search.hibernate.entityDAO;

import org.ezcampus.search.hibernate.entity.CourseData;
import org.ezcampus.search.hibernate.entity.Word;
import org.ezcampus.search.hibernate.entity.WordMap;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.tinylog.Logger;

public class WordMapDAO
{
	/**
	 * Inserts the given mapping, assumes you are already in a transaction
	 * @param word The word_id 
	 * @param courseData The course_data_id
	 * @param session The active database session
	 */
	public static void insertMap_NT(Session session, Word word, CourseData courseData) 
	{
		final String HQL = "FROM WordMap m WHERE m.word = :word AND m.courseData = :cd";
		
		WordMap map = session.createQuery(HQL, WordMap.class)
				.setParameter("word", word)
				.setParameter("cd", courseData)
				.setMaxResults(1)
				.getSingleResultOrNull();
		
		if(map != null)
		{
			map.increaseCountBy(1);
		}
		else 
		{
			map = new WordMap(word, courseData, 1);
		}

		session.persist(map);
	}
	
	
	/**
	 * Removes all the word mappings for the given course data
	 * @param session
	 * @param courseData
	 */
	public static void removeMap(Session session, CourseData courseData) 
	{
		Transaction tx = session.beginTransaction();
		
		final String HQL = "FROM WordMap m WHERE m.courseData = :cd";
		
		session.createQuery(HQL, WordMap.class)
				.setParameter("cd", courseData)
				.getResultStream()
				.forEach(x -> session.remove(x));
		
		tx.commit();
	}
}
