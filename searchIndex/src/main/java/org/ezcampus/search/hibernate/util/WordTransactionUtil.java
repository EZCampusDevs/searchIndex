package org.ezcampus.search.hibernate.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.ezcampus.search.data.StringHelper;
import org.ezcampus.search.hibernate.entity.CourseData;
import org.ezcampus.search.hibernate.entity.Word;
import org.ezcampus.search.hibernate.entityDAO.WordDAO;
import org.hibernate.Session;

public class WordTransactionUtil
{
	public static final String WORD_INSERT_SPLIT_REGEX = "[\\s,_]";
	
	private ArrayList<WordMap2> map;
	
	private static class WordMap2 {
		public String word;
		public Word wordObj;
		public CourseData data;
		
		public WordMap2() {
			
		}
		
		public WordMap2(String w, CourseData d) {
			this.word = w;
			this.data = d;
		}
	}

	public WordTransactionUtil()
	{
		map = new ArrayList<>();
	}

	public WordTransactionUtil(int initialCapacity)
	{
		map = new ArrayList<>(initialCapacity);
	}
	
	
	public void insertWord(String word, CourseData data) 
	{
		if(word == null) return;
		
		for (String v : word.split(WORD_INSERT_SPLIT_REGEX))
		{
			if (v == null || v.isBlank())
			{
				continue;
			}

			map.add(new WordMap2(StringHelper.cleanWord(v), data));
		}
	}
	
	public void persistWords(Session session) 
	{
		HashMap<String, Word> wordsToInsert = new HashMap();
		
		for(WordMap2 wm : this.map)
		{
			Word existingWord = session.byNaturalId(Word.class).using("word", wm.word).load();
			
			if(existingWord == null) {
				wordsToInsert.put(wm.word, null);
				continue;
			}
			
			wm.wordObj = existingWord;
		}
//		Set<String> wordsToInsert = new HashSet<>();
	}
}
