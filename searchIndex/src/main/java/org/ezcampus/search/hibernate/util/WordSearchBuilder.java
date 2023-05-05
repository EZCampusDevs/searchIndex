package org.ezcampus.search.hibernate.util;

import java.util.ArrayList;

import org.ezcampus.search.hibernate.entity.Word;
import org.hibernate.Session;

public class WordSearchBuilder {

	int course_id;
	ArrayList<String> entries; //String entries from Data
	Session session;
	
	public WordSearchBuilder(int cId, ArrayList<String> entries, Session session) {
		this.course_id = cId;
		this.entries = entries;
		this.session = session;
		Print();
	}
	
	public void Print() {
		System.out.println("CID: "+course_id);
		
		for(int j = 0; j < entries.size(); j++) {
			System.out.println("W: "+entries.get(j));
		}
	}
	
	private void buildWords() {
		
		//TODO: Parse `entries` into `parsedEntries` for better word coverage in lookup
		
		ArrayList<String> parsedEntries = entries;
		
	    for (String entry : parsedEntries) {
	        String[] words = entry.split("\\s+");
	        for (String word : words) {
	            // Check if the word already exists in the database
	            Word existingWord = session.byNaturalId(Word.class).using("word", word).load();
	            if (existingWord != null) {
	                continue;
	            }
	            Word newWord = new Word(word);
	            session.persist(newWord);
	        }
	    }
	    session.flush(); // commit changes to the database
	}

	
}
