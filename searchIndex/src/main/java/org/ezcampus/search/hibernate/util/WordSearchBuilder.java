package org.ezcampus.search.hibernate.util;

import java.util.ArrayList;

import org.ezcampus.search.hibernate.entity.CourseData;
import org.ezcampus.search.hibernate.entity.Word;
import org.ezcampus.search.hibernate.entity.WordMap;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class WordSearchBuilder {

	CourseData courseData;
	ArrayList<String> wordList; // String entries from Data
	Session session;

	private ArrayList<String> parseEntries(ArrayList<String> entries) {

		// TODO: Better parsing here, actually create the word list

		ArrayList<String> cleanedEntries = new ArrayList<>();
		for (String entry : entries) {
			if (entry != null) {
				cleanedEntries.add(entry);
			}
		}
		return cleanedEntries;
	}

	public WordSearchBuilder(CourseData cId, ArrayList<String> entries, Session session) {
		this.courseData = cId;
		this.wordList = parseEntries(entries);
		this.session = session;
		Print();
		buildWords();
	}

	public void Print() {
		System.out.println("cId" + courseData.getCourseDataId());

		for (int j = 0; j < wordList.size(); j++) {
			System.out.println("W: " + wordList.get(j));
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
							.setParameter("entry", entry).uniqueResult();

					if (existingWord == null) {
						Word newWord = new Word(entry);
						session.persist(newWord); // Persist new word to `Word` table

						// At this point we know there are no Records for `WordMap` associated with newWord

						WordMap wM = new WordMap(newWord, 1, courseData);
						session.persist(wM); // Persist Word Map associated to new Word & Course Data Id

					} else { // Word already exists

						// Check if the WordMap already exists in the database given the Entry & CourseDataId
						WordMap existingWordMap = session
								.createQuery("FROM WordMap WHERE word = :entry AND courseDataId = :cid", WordMap.class)
								.setParameter("entry", existingWord).setParameter("cid", courseData).uniqueResult();

						//Create `WordMap` record for existingWord if it doesn't already have one
						if (existingWordMap == null) {
							WordMap wM = new WordMap(existingWord, 1, courseData);
							session.persist(wM); 
						}
					}

					//
				}
				tx.commit();
			} catch (HibernateException e) {
				if (tx != null) {
					tx.rollback();
				}
				e.printStackTrace();
				
			} //End of Catch
			
		} //End of Session Scope
		
	} // End of Build Words

}
