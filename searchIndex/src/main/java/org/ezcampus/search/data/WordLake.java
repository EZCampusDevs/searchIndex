package org.ezcampus.search.data;

import java.util.ArrayList;

import org.ezcampus.search.hibernate.entity.Word;
import org.ezcampus.search.hibernate.util.HibernateUtil;
import org.hibernate.Session;

public class WordLake
{
	private ArrayList<String> wordsToAdd = new ArrayList<String>();
	private BinaryTreeArray mappedWords = new BinaryTreeArray();
	private Session session = null;
	
	public void init() 
	{
		if(this.session == null)
			this.session = HibernateUtil.getSessionFactory().openSession();
		
		if(!this.session.isOpen())
			this.session = HibernateUtil.getSessionFactory().openSession();
	}
	
	public void addWord(String word) 
	{
		if(this.mappedWords.contains(word)) 
			return;
		
		this.init();
		
		Word existingWord = session.byNaturalId(Word.class)
				   .using("word", word)
				   .load();
		
		if(existingWord == null) 
		{
			this.wordsToAdd.add(word);
		} 
		else 
		{
			this.mappedWords.add(existingWord);
		}
	}

}
