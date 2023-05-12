package org.ezcampus.search.core;

import java.util.List;

import org.ezcampus.search.hibernate.entity.Course;
import org.ezcampus.search.hibernate.entity.Word;
import org.ezcampus.search.hibernate.entity.WordMap;
import org.ezcampus.search.hibernate.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.tinylog.Logger;

public abstract class SearchHandler
{

	public static void print(List<WordMap> m)
	{

		for (WordMap wm : m)
		{

		}

	}

	public static void search(String searchTerm)
	{

		// Look up in the word index

		try (Session session = HibernateUtil.getSessionFactory().openSession())
		{
			Transaction tx = null;
			try
			{
				tx = session.beginTransaction();

				// All words in the `Word` table should be unique, therefore a Unique Result
				// whilst querying for the Word
				// TODO: possibly switch searching for word to Natural Language Processing???

				Word matchingWord = session.createQuery("FROM Word w WHERE w.word = :targetWord", Word.class)
						.setParameter("targetWord", searchTerm).uniqueResult();

				List<WordMap> matchingEntries = session
						.createQuery("FROM WordMap wm WHERE wm.word = :targetId", WordMap.class)
						.setParameter("targetId", matchingWord.getId()).list();

			}
			catch (HibernateException e)
			{
				if (tx != null && tx.isActive())
				{
					tx.rollback();
				}
				Logger.error(e);
				e.printStackTrace(); // Prevent Damage to Data caused by Hibernate Error
			}
		}

	}

}
