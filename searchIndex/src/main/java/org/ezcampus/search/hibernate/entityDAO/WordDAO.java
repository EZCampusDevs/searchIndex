package org.ezcampus.search.hibernate.entityDAO;

import org.ezcampus.search.data.StringHelper;
import org.ezcampus.search.hibernate.entity.CourseData;
import org.ezcampus.search.hibernate.entity.Word;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class WordDAO
{
	/**
	 * Inserts the given word into the database, assumes there is already a transaction
	 * @param wordString The word to insert
	 * @param session The database connection
	 * @return The inserted word, or the existing word
	 */
	public static Word insertWord_NT(Session session, String wordString)
	{
		if(wordString == null) 
			return null;
		
		String cleaned = StringHelper.cleanWord(wordString);
		
		if (cleaned.isEmpty()) 
			return null;
	
		Word existingWord = session.byNaturalId(Word.class)
								   .using("word", cleaned)
								   .load();
		
		if (existingWord != null)
			return existingWord;
		

		Word newWord = new Word(cleaned);
		
        session.persist(newWord);
	
		return newWord;
	}
	
	
	/**
	 * Inserts the word and a mapping associating the word to the given CourseData
	 * 
 	 * @param session The database connection
 	 * @param wordString The word to insert and associate
 	 * @param courseData The CourseData to associate with the word
	 * @return The inserted word, or the existing word
	 */
	public static void insertLinkWord(Session session, String wordString, CourseData courseData) 
	{	
		Transaction tx = session.getTransaction();
		
		tx.begin();
		
		Word word = insertWord_NT(session, wordString);
		
		if(word != null) 
			WordMapDAO.insertMap_NT(session, word, courseData);
		
		tx.commit();
	}
	
	
	/**
	 * Inserts the word and a mapping associating the word to the given CourseData
	 * 
 	 * @param session The database connection
 	 * @param wordString The word to insert and associate
 	 * @param courseData The CourseData to associate with the word
	 * @return The inserted word, or the existing word
	 */
	public static void insertLinkWordOptimized(Session session, String wordString, CourseData courseData) 
	{	
		if(wordString == null) 
			return;
		
		String cleaned = StringHelper.cleanWord(wordString);
	
		if (cleaned.isEmpty()) 
			return;
		
	
		Word existingWord = session.byNaturalId(Word.class)
								   .using("word", cleaned)
								   .load();
		
		Transaction tx = session.getTransaction();
		
		tx.begin();
		
		if (existingWord != null)
		{	
			WordMapDAO.insertMap_NT(session, existingWord, courseData);
			
			tx.commit();
			
			return;
		}
		
		Word newWord = new Word(cleaned);
		
        session.persist(newWord);
	
        WordMapDAO.insertMap_NT(session, newWord, courseData);
		
        tx.commit();
	}
}
