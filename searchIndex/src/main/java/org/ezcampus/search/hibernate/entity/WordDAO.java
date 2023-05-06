package org.ezcampus.search.hibernate.entity;

import org.ezcampus.search.hibernate.util.SessionUtil;
import org.hibernate.Session;
import org.tinylog.Logger;

public class WordDAO {

	public static String getWord(String input) {
		
		try (Session session = SessionUtil.getSessionFactory().openSession()) 
		{
			Logger.debug("In session try statement");
			
			Word existingWord = session.createQuery("FROM Word WHERE word = :entry", Word.class)
					.setParameter("entry", input).uniqueResult();
			
			return existingWord.toString();
		}
		
	}
	
}
