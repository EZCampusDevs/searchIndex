package org.ezcampus.search.hibernate.util;

import java.util.ArrayList;
import java.util.List;

import org.ezcampus.search.hibernate.entity.Word;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class WordSearchBuilder {

	int course_id;
	ArrayList<String> wordList; //String entries from Data
	Session session;
	
	
	private ArrayList<String> parseEntries(ArrayList<String> entries) {
		
		//TODO: Better parsing here, actually create the word list
		
	    ArrayList<String> cleanedEntries = new ArrayList<>();
	    for (String entry : entries) {
	        if (entry != null) {
	            cleanedEntries.add(entry);
	        }
	    }
	    return cleanedEntries;
	}
	
	
	public WordSearchBuilder(int cId, ArrayList<String> entries, Session session) {
		this.course_id = cId;
		this.wordList = parseEntries(entries);
		this.session = session;
		Print();
		buildWords();
	}
	
	public void Print() {
		System.out.println("CID: "+course_id);
		
		for(int j = 0; j < wordList.size(); j++) {
			System.out.println("W: "+wordList.get(j));
		}
	}
	
	private void buildWords() {
		
	    try (Session session = SessionUtil.getSessionFactory().openSession()) {
	        Transaction tx = null;
	        try {
	            tx = session.beginTransaction();
	            for (String entry : wordList) {
	                // Check if the word already exists in the database
	            	Word existingWord = session.createQuery("FROM Word WHERE word = :entry", Word.class)
                            .setParameter("entry", entry)
                            .uniqueResult();
	                
	                if (existingWord == null) {
	                    Word newWord = new Word(entry);
	                    session.persist(newWord);
	                }
	            }
	            tx.commit();
	        } catch (HibernateException e) {
	            if (tx != null) {
	                tx.rollback();
	            }
	            e.printStackTrace();
	        }
	    }
	} //End of Build Words

	
}
