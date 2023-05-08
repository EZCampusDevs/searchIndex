package org.ezcampus.search.hibernate.entity;

import java.io.Serializable;

import org.ezcampus.search.data.StringHelper;
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

	public static int insertWord(String wordString)
	{
		if(wordString == null) return -1;
		String cleaned = StringHelper.cleanWord(wordString);
		
		if (cleaned.isEmpty()) return -1;
		
		
		Transaction tx = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession())
		{
			tx = session.beginTransaction();

			// Check if the word already exists in the database
			Word existingWord = session.byNaturalId(Word.class).using("word", wordString).load();
			if (existingWord != null)
			{
				return existingWord.getId();
			}

			Word newWord = new Word(cleaned);
            session.persist(newWord);
            int id = newWord.getId();

			tx.commit();

			return id;
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
}
