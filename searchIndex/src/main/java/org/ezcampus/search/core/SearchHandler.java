package org.ezcampus.search.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.ezcampus.search.core.models.CourseDataResult;
import org.ezcampus.search.hibernate.entity.Course;
import org.ezcampus.search.hibernate.entity.CourseData;
import org.ezcampus.search.hibernate.entity.CourseFaculty;
import org.ezcampus.search.hibernate.entity.Word;
import org.ezcampus.search.hibernate.entity.WordMap;
import org.ezcampus.search.hibernate.util.HibernateUtil;
import org.hibernate.Session;
import org.tinylog.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class SearchHandler
{
	public static void main(String[] args)
	{

		List<CourseDataResult> results = search("paula", 1, 50);

		ObjectMapper objectMapper = new ObjectMapper();

		try
		{
			String jsonArray = objectMapper.writeValueAsString(results);

			Logger.info("Searhc results json: {}", jsonArray);
		}
		catch (JsonProcessingException e)
		{
			Logger.error(e);
		}
	}

	public static List<CourseDataResult> loadIn(List<CourseData> results)
	{
		ArrayList<CourseDataResult> cdr = new ArrayList<>(results.size());

		if (results.size() == 0)
			return cdr;

		try (Session session = HibernateUtil.getSessionFactory().openSession())
		{
			for (CourseData courseData : results)
			{
				Course course = courseData.getCourse();

				List<CourseFaculty> facultyQuery = session
						.createQuery("SELECT cf FROM CourseFaculty cf WHERE cf.courseDataId = :courseData", CourseFaculty.class)
						.setParameter("courseData", courseData)
						.getResultList();


				CourseDataResult combinedEntry = new CourseDataResult(course, courseData, facultyQuery);

				cdr.add(combinedEntry);
			}
		}

		return cdr;
	}

	public static List<CourseDataResult> search(String searchTerm, int page, int resultsPerPage)
	{
		// Look up in the word index
		ArrayList<CourseData> relevantCDs = new ArrayList<>();

		if (searchTerm == null)
			return loadIn(relevantCDs);

		// Calculate the offset based on the page number
		int pageoffset = resultsPerPage * (page - 1);

		try (Session session = HibernateUtil.getSessionFactory().openSession())
		{

			// All words in the `Word` table should be unique, therefore a Unique Result
			// whilst querying for the Word

			String[] searchTerms = Arrays.stream(searchTerm.split("\\s+")) // Split the searchTerm into an array of
																		// words
					.filter(word -> !word.trim().isEmpty()) // Filter out empty strings or strings with only blank
															// spaces
					.toArray(String[]::new); // Collect the filtered words back into a new array

			for (String word : searchTerms)
			{
				Word matchingWord = session.createQuery("FROM Word w WHERE w.word = :targetWord", Word.class)
						.setParameter("targetWord", word).uniqueResult();

				if (matchingWord == null)
				{
					continue;
				}

				Logger.debug("WORD: {} ID: {}", matchingWord.getWordString(), matchingWord.getId());

				// .setFirstResult offsets the first result by `offset`, and setMaxResults set's
				// `resultsPerPage`

				List<WordMap> matchingEntries = session
						.createQuery("FROM WordMap wm WHERE wm.word = :targetId", WordMap.class)
						.setParameter("targetId", matchingWord)
						.setFirstResult(pageoffset)
						.setMaxResults(resultsPerPage)
						.list();

				// Add Matching entries into relevant course data list (relevantCDs)

				for (WordMap wordMap : matchingEntries)
				{
					CourseData courseData = wordMap.getCourseData();

					boolean isNewEntry = true;
					
					for (CourseData relevantCD : relevantCDs)
					{
						if (relevantCD.equals(courseData))
						{
							relevantCD.ranking += wordMap.getCount();
							isNewEntry = false;
							break;
						}
					}

					if (isNewEntry)
					{
						relevantCDs.add(courseData);
					}
				}
			}
		}

		return loadIn(relevantCDs);

	}

}
