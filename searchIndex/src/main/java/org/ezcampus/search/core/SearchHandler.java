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
import org.tinylog.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.ws.rs.core.Response;

public abstract class SearchHandler {

	public static ArrayList<CourseDataResult> loadIn(ArrayList<CourseData> results) {

		ArrayList<CourseDataResult> cdr = new ArrayList<>();

		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			Transaction tx = null;
			try {
				tx = session.beginTransaction();

				for (CourseData courseData : results) {

					// Get the Course object associated with the CourseData

					Course course = courseData.getCourse();

					// Query the CourseFaculty for the given CourseData

					List<CourseFaculty> facultyQuery = session
							.createQuery("SELECT cf FROM CourseFaculty cf WHERE cf.courseDataId = :courseData",
									CourseFaculty.class)
							.setParameter("courseData", courseData).getResultList();

					// Do whatever you need with the Course and CourseFaculty data

					CourseDataResult combinedEntry = new CourseDataResult(course, courseData, facultyQuery);

					cdr.add(combinedEntry);
				}

			} catch (HibernateException e) {
				if (tx != null) {
					tx.rollback();
				}
				e.printStackTrace(); // Prevent Damage to Data caused by Hibernate Error
			}
		} // endof main try

		return cdr;
	}

	public static void main(String[] args) {

		ArrayList<CourseDataResult> results = search("rupinder", 1, 5);

		ObjectMapper objectMapper = new ObjectMapper();

		try {
			String jsonArray = objectMapper.writeValueAsString(results);

			System.out.println(jsonArray);

		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

	}

	public static ArrayList<CourseDataResult> search(String searchTerm, int page, int resultsPerPage) {

		// Page Offset:
		int offset = resultsPerPage * (page - 1); // Calculate the offset based on the page number

		// Look up in the word index
		ArrayList<CourseData> results = new ArrayList<>();

		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			Transaction tx = null;
			try {
				tx = session.beginTransaction();

				// All words in the `Word` table should be unique, therefore a Unique Result
				// whilst querying for the Word

				ArrayList<CourseData> relevantCDs = new ArrayList(); // Relevant results

				String[] searchTerms = Arrays.stream(searchTerm.split(" ")) // Split the searchTerm into an array of
																			// words
						.filter(word -> !word.trim().isEmpty()) // Filter out empty strings or strings with only blank
																// spaces
						.toArray(String[]::new); // Collect the filtered words back into a new array

				for (String word : searchTerms) {

					Word matchingWord = session.createQuery("FROM Word w WHERE w.word = :targetWord", Word.class)
							.setParameter("targetWord", word)
							.uniqueResult();

					if (matchingWord == null) {
						continue;
					}

					System.out.println("WORD: " + matchingWord.getWordString() + "id: " + matchingWord.getId());

					// .setFirstResult offsets the first result by `offset`, and setMaxResults set's
					// `resultsPerPage`

					List<WordMap> matchingEntries = session
							.createQuery("FROM WordMap wm WHERE wm.word = :targetId", WordMap.class)
							.setParameter("targetId", matchingWord)
							.setFirstResult(offset)
							.setMaxResults(resultsPerPage)
							.list();

					// Add Matching entries into relevant course data list (relevantCDs)

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
					//
				}

				results = relevantCDs;
			} catch (HibernateException e) {
				if (tx != null) {
					tx.rollback();
				}
				e.printStackTrace(); // Prevent Damage to Data caused by Hibernate Error
			}
		}

		return loadIn(results);

	}

}
