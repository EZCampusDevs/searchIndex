package org.ezcampus.search.hibernate.entityDAO;

import java.io.Serializable;

import org.ezcampus.search.data.StringHelper;
import org.ezcampus.search.hibernate.entity.CourseData;
import org.ezcampus.search.hibernate.entity.Word;
import org.ezcampus.search.hibernate.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.tinylog.Logger;

public class WordDAO
{

	public static Word getWord(String input)
	{
		try (Session session = HibernateUtil.getSessionFactory().openSession())
		{
			Logger.debug("In session try statement");

			return session.createQuery("FROM Word WHERE word = :entry", Word.class).setParameter("entry", input)
					.setMaxResults(1).getSingleResultOrNull();
		}
	}

	public static String getWordString(String input)
	{
		Word w = getWord(input);

		if (w == null)
		{
			return null;
		}

		return w.getWordString();
	}

	public static Integer getWordId(String input)
	{
		Word w = getWord(input);

		if (w == null)
		{
			return null;
		}

		return w.getId();
	}

	/**
	 * Inserts the given word into the database, assumes there is already a transaction
	 * @param wordString The word to insert
	 * @param session The database connection
	 * @return The inserted word, or the existing word
	 */
	public static Word insertWord_NT(String wordString, Session session)
	{
		if(wordString == null) 
			return null;
		
		String cleaned = StringHelper.cleanWord(wordString);
		
		if (cleaned.isEmpty()) 
			return null;
	
		// Check if the word already exists in the database
		Word existingWord = session.byNaturalId(Word.class).using("word", cleaned).load();
		
		if (existingWord != null)
		{
			return existingWord;
		}

		Word newWord = new Word(cleaned);
		
        session.persist(newWord);
	
		return newWord;
	}
	
	public static Word insertWord(String wordString, Session session)
	{
		if(wordString == null) return null;
		String cleaned = StringHelper.cleanWord(wordString);
		
		if (cleaned.isEmpty()) return null;
		
		
		Transaction tx = null;
		try
		{
			// Check if the word already exists in the database
			Word existingWord = session.byNaturalId(Word.class).using("word", cleaned).load();
			if (existingWord != null)
			{
				return existingWord;
			}

			tx = session.beginTransaction();

			Word newWord = new Word(cleaned);
            session.persist(newWord);
			tx.commit();

			return newWord;
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

	public static void linkWord(int wordId, int courseDataId)
	{

	}
	
	//Inserts a word, then inserts linked wordMap entry
	
	public static void insertLinkWord(String wordString, CourseData courseData, Session session) 
	{
		Transaction tx = session.getTransaction();
		
		if(!tx.isActive())
			tx.begin();
		
		Word word = insertWord_NT(wordString, session);
		
		if(word == null) {
			Logger.debug("Cannot insertLinkWord because word {} was null after inserting", wordString);
			return;
		}
		
		WordMapDAO.insertMap_NT(word, courseData, session);
		
		tx.commit();
	}
}
