package org.ezcampus.search.data.inserter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.ezcampus.search.System.GlobalSettings;
import org.ezcampus.search.data.StringHelper;
import org.ezcampus.search.hibernate.entity.CourseData;
import org.ezcampus.search.hibernate.entity.Word;
import org.ezcampus.search.hibernate.entity.WordMap;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.tinylog.Logger;

public class HashMapWordInserter implements WordInserter
{
	HashMap<String, Integer> wordCache = new HashMap<String, Integer>();
	
	@Override
	public void init(Session session)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void insertWord(Session session, String value)
	{
		if(value == null)
			return;
		
//		value = StringHelper.cleanWord(value);
		
		if(value.isBlank())
			return;
		
		Integer count = this.wordCache.get(value);
		
		if(count == null) 
		{
			wordCache.put(value, 1);
		}
		else 
		{
			wordCache.put(value, count + 1);
		}
	}

	@Override
	public void associateWords(Session session, CourseData courseData)
	{
		Logger.debug("About to insert select {} words", wordCache.size());
		
		ArrayList<Object[]> words = insertWords(session, wordCache);
		
		Transaction tx = session.beginTransaction();
		 
		for(Object[] o : words) 
		{
			Word w = (Word)o[0];
			int count = (int)o[1];
			
			final String HQL = "FROM WordMap m WHERE m.word = :word AND m.courseData = :cd";
			
			WordMap map = session.createQuery(HQL, WordMap.class)
					.setParameter("word", w)
					.setParameter("cd", courseData)
					.getSingleResultOrNull();
			
			if(map != null)
			{
				map.increaseCountBy(count);
			}
			else 
			{
				map = new WordMap();
				map.setCount(count);
				map.setCourseData(courseData);
				map.setWord(w);
			}
			
			session.persist(map);
		}
		
		tx.commit();
		
		this.wordCache.clear();
	}
	
	private static ArrayList<Object[]> insertWords(Session session, HashMap<String, Integer> cache) 
	{
		ArrayList<Object[]> words = new ArrayList<Object[]>(cache.size());
		
		List<Word> existingWords = session.createQuery("FROM Word WHERE word IN :words", Word.class)
	            .setParameter("words", cache.keySet())
	            .getResultList();
 
		Logger.debug("Batch selected {} words from the db", existingWords.size());
		
		for(Word w : existingWords)
		{
			words.add(new Object[] { w, cache.get(w.getWordString()) });
			
			cache.remove(w.getWordString());
		}
		
		if(cache.size() == 0)
			return words;
		
		Logger.debug("Batch insert {} words into the db", cache.size());
		
		Transaction tx = session.beginTransaction();
		
		cache.forEach((word, count) -> {
	 
			if(GlobalSettings.DEBUG_LOG_INSERTING_WORDS)
				Logger.debug("Inserting word {}", word);
			
			Word newWord = new Word();
	           
			newWord.setWordString(word);
       
			if(count == null) count = 1;
			
			words.add(new Object[] { newWord, count });
       
			session.persist(newWord);
       
		});

		tx.commit();
       
		return words;
	}

}
