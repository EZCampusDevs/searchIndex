package org.ezcampus.search.hibernate.entity;

import org.ezcampus.search.hibernate.util.SessionUtil;
import org.hibernate.Session;

public class WordDAO {

	public static String getWord(String input) {
		
		try (Session session = SessionUtil.getSessionFactory().openSession()) {
			
			Word existingWord = session.createQuery("FROM Word WHERE word = :entry", Word.class)
					.setParameter("entry", input).uniqueResult();
			
			return existingWord.toString();
		}
		
	}
	
}
