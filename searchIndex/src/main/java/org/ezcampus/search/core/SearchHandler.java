package org.ezcampus.search.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.ezcampus.search.hibernate.entity.Course;
import org.ezcampus.search.hibernate.entity.CourseData;
import org.ezcampus.search.hibernate.entity.CourseFaculty;
import org.ezcampus.search.hibernate.entity.Word;
import org.ezcampus.search.hibernate.entity.WordMap;
import org.ezcampus.search.hibernate.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public abstract class SearchHandler {

	public static void print(List<WordMap> m ) {
		
		for (WordMap wm : m) {
			
			
			
		}

	}
	
	public static ArrayList<CourseDataResult> loadIn(ArrayList<CourseData> results) {
		
		ArrayList<CourseDataResult> cdr = new ArrayList();		
		
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			Transaction tx = null;
			try {
				tx = session.beginTransaction();
				
				for (CourseData courseData : results) {

					// Get the Course object associated with the CourseData
				    Course course = courseData.getCourse();

				    
				    
				    // Query the CourseFaculty for the given CourseData
				    List<CourseFaculty> facultyQuery = session.createQuery(
				            "SELECT cf FROM CourseFaculty cf WHERE cf.courseDataId = :courseData",
				            CourseFaculty.class).setParameter("courseData", courseData).getResultList();

				    // Do whatever you need with the Course and CourseFaculty data
				    System.out.println("Course: " + course.getCourseCode());
				    
				    
				    CourseDataResult combinedEntry = new CourseDataResult(course, courseData, facultyQuery);
				    cdr.add(combinedEntry);
				}
			
			
			} catch (HibernateException e) { if (tx != null) {tx.rollback();} 
				e.printStackTrace(); //Prevent Damage to Data caused by Hibernate Error
			}
			
		} //endof main try
		
		return cdr;
	}
	
	public static void main(String[] args) {
		
		
		ArrayList<CourseData> results = search("Rupinder Brar");
		
		ArrayList<CourseDataResult> parsedResults = loadIn(results);
		
		for(CourseData entry : results) { 
			
			System.out.println(entry.getCrn());
			System.out.println(entry.getCourseTitle());
			System.out.println(entry.ranking);
		}
		
		
			
	}
		
		
 	
	
	
	public static ArrayList<CourseData> search(String searchTerm) {
		
		//Look up in the word index
		
		ArrayList<CourseData> results = new ArrayList();
		
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			Transaction tx = null;
			try {
				tx = session.beginTransaction();
				
				//All words in the `Word` table should be unique, therefore a Unique Result whilst querying for the Word
				
				ArrayList<CourseData> relevantCDs = new ArrayList(); //Relevant results
				
				
				String[] searchTerms = Arrays.stream(searchTerm.split(" "))  // Split the searchTerm into an array of words
                        .filter(word -> !word.trim().isEmpty())  // Filter out empty strings or strings with only blank spaces
                        .toArray(String[]::new);  // Collect the filtered words back into a new array

				for (String word : searchTerms) {
	            
				
					Word matchingWord = session
						    .createQuery("FROM Word w WHERE w.word = :targetWord", Word.class)
						    .setParameter("targetWord", word)
						    .uniqueResult();	
					
					System.out.println("WORD: "+ matchingWord.getWordString()+"id: "+matchingWord.getId());
				
					List<WordMap> matchingEntries = session
						    .createQuery("FROM WordMap wm WHERE wm.word = :targetId", WordMap.class)
						    .setParameter("targetId", matchingWord)
						    .list();
				
					for (WordMap wordMap : matchingEntries) {
				        CourseData courseData = wordMap.getCourseData();
				        
				        boolean isNewEntry = true;
	
				        for (CourseData relevantCD : relevantCDs) {
				            if (relevantCD.equals(courseData)) {
				                relevantCD.ranking += 1;
				                isNewEntry = false;
				                break;
				            }
				        }
	
				        if (isNewEntry) {
				            relevantCDs.add(courseData);
				        }
				    }
				
				}
				
				
				results = relevantCDs;
			} catch (HibernateException e) { if (tx != null) {tx.rollback();} 
				e.printStackTrace(); //Prevent Damage to Data caused by Hibernate Error
			}
		}
		
		return results;
		
		
	}
	
}
