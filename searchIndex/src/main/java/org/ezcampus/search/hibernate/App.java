package org.ezcampus.search.hibernate;

import java.util.List;

import org.ezcampus.search.System.ResourceLoader;
import org.ezcampus.search.hibernate.entity.ClassType;
import org.ezcampus.search.hibernate.entity.Term;
import org.ezcampus.search.hibernate.entity.Word;
import org.ezcampus.search.hibernate.util.SessionUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

public class App
{
	// https://www.javaguides.net/2018/11/hibernate-hello-world-tutorial.html

	public static void insertWords(List<String> words)
	{
		try (Session session = SessionUtil.getSessionFactory().openSession())
		{
			Transaction tx = session.beginTransaction();
			for (String word : words)
			{
				try
				{
					session.persist(new Word(word));
				}
				catch (ConstraintViolationException ex)
				{
					// Log message and continue inserting the rest of the words
					System.out.printf("Word '%s' already exists, ignoring.%n", word);
				}
			}
			tx.commit();
		}
		catch (HibernateException ex)
		{
			ex.printStackTrace();
		}
	}

	public static void main(String[] args)
	{
		ResourceLoader.loadTinyLogConfig();

//		List<String> word1s = Arrays.asList(
//				"new", "new2", "new3", "hello", "world", "nyah", "uwu", "test", "uwu", "nyah", "test", "alskd", "other"
//		);
//		insertWords(word1s);

		try (Session session = SessionUtil.getSessionFactory().openSession())
		{
			
			
			
			
			List<Word> words = session.createQuery("FROM Word", Word.class).list();
			for (Word word : words)
			{
				System.out.println(word);
			}
			
			List<Term> terms = session.createQuery("FROM Term", Term.class).list();
			List<ClassType> cTypes = session.createQuery("FROM ClassType", ClassType.class).list();
			
			
			
		}
		catch (HibernateException ex)
		{
			ex.printStackTrace();
		}
	}
}
