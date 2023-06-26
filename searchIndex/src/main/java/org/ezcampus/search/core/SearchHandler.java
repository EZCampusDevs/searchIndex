package org.ezcampus.search.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.ezcampus.search.System.GlobalSettings;
import org.ezcampus.search.System.ResourceLoader;
import org.ezcampus.search.core.models.response.CourseDataResult;
import org.ezcampus.search.data.StringHelper;
import org.ezcampus.search.hibernate.entity.Course;
import org.ezcampus.search.hibernate.entity.CourseData;
import org.ezcampus.search.hibernate.entity.CourseFaculty;
import org.ezcampus.search.hibernate.entity.Meeting;
import org.ezcampus.search.hibernate.entity.Word;
import org.ezcampus.search.hibernate.entity.WordMap;
import org.ezcampus.search.hibernate.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.tinylog.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class SearchHandler
{
	// public static void main(String[] args)
	// {

	// 	searchNative("", 0, 0, 0);
	// }

	public static List<CourseDataResult> loadIn(List<CourseData> results, Session session)
	{
		ArrayList<CourseDataResult> cdr = new ArrayList<>(results.size());

		if (results.size() == 0)
			return cdr;

		for (CourseData courseData : results)
		{
			Course course = courseData.getCourse();

			List<CourseFaculty> facultyQuery = session.createQuery(
					"SELECT cf FROM CourseFaculty cf WHERE cf.courseDataId = :courseData", CourseFaculty.class
			).setParameter("courseData", courseData).getResultList();

			List<Meeting> meetingQuery = session
					.createQuery("SELECT m FROM Meeting m WHERE m.courseDataId = :courseData", Meeting.class)
					.setParameter("courseData", courseData).getResultList();

			CourseDataResult combinedEntry = new CourseDataResult(course, courseData, facultyQuery, meetingQuery);
			
			cdr.add(combinedEntry);
		}

		return cdr;
	}

	public static CourseDataResult loadSingleCDR(CourseData courseData, Session session){
			Course course = courseData.getCourse();

			List<CourseFaculty> facultyQuery = session.createQuery(
					"SELECT cf FROM CourseFaculty cf WHERE cf.courseDataId = :courseData", CourseFaculty.class
			).setParameter("courseData", courseData).getResultList();

			List<Meeting> meetingQuery = session
					.createQuery("SELECT m FROM Meeting m WHERE m.courseDataId = :courseData", Meeting.class)
					.setParameter("courseData", courseData).getResultList();

			CourseDataResult combinedEntry = new CourseDataResult(course, courseData, facultyQuery, meetingQuery);
			return combinedEntry;
	}

	public static void rank(List<WordMap> matchingEntries, List<CourseData> relevantCDs)
	{
		for (WordMap wordMap : matchingEntries)
		{
			CourseData courseData = wordMap.getCourseData();

			boolean isNewEntry = true;

			for (CourseData relevantCD : relevantCDs)
			{
				if (relevantCD.equals(courseData))
				{
					relevantCD.ranking += wordMap.getCount(); //TODO: Replace this with Leveinsthein distance?
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

	
	public static List<CourseDataResult> searchFuzzy2(String searchTerm, int page, int resultsPerPage, int termId)
	{
		// Look up in the word index
		ArrayList<CourseData> relevantCDs = new ArrayList<>();

		if (searchTerm == null)
			return List.of();

		// Calculate the offset based on the page number
		int pageoffset = resultsPerPage * (page - 1);

		try (Session session = HibernateUtil.getSessionFactory().openSession())
		{
			Arrays.stream(searchTerm.split("\\s+")) //Stream words in the search term "CALC II" -> Stream of strings [ "CALC" , "II"]
				.map(StringHelper::cleanWord) 
				.filter(word -> !word.isEmpty() && word.length() >= 3) 
				.forEach(word -> 
				{
					String query = "SELECT wm FROM WordMap wm " +
					        "JOIN wm.word w " +
					        "JOIN wm.courseData cd " +
					        "JOIN cd.course c " +
					        "JOIN c.term t " +
					        "WHERE w.word LIKE :targetWord " +
					        "AND t.termId = :termId";
					
					List<WordMap> matchingEntries = session.createQuery(query, WordMap.class)
					        .setParameter("targetWord", "%" + word + "%")
					        .setParameter("termId", termId)
					        .getResultList();
					
					rank(matchingEntries, relevantCDs);
				});
			return loadIn(relevantCDs, session);
		}
	}
	
public static List<CourseDataResult> searchNative(String searchTerm, int page, int resultsPerPage, int termId) {
    
	//! ############### Assertions: ###############

    if (searchTerm.trim().length() < 3) {
        return new ArrayList<>();
    } //Trim and check if the entire searchTerm is lesser than 3


	if(page < 0 || resultsPerPage < 1){
		return new ArrayList<>();
	} //Make sure Limit & Offsets make sense

	//! ############### Filtering of searchTerm: ############### 

	List<String> wordsVector = Arrays.stream(searchTerm.split("\\s+"))
			.map(StringHelper::cleanWord)
			.filter(word -> !word.isEmpty())
			.collect(Collectors.toList());

	String[] words = wordsVector.toArray(new String[0]);

	ArrayList<CourseDataResult> CDR_Query = new ArrayList<>();

    try (Session session = HibernateUtil.getSessionFactory().openSession()) {


        // prepare your LIKE clauses
        String whereClause = "";
        for (int i = 0; i < words.length; i++) {
            whereClause += "tbl_word.word LIKE :wordStart" + i + " OR tbl_word.word LIKE :wordEnd" + i;
            if (i < words.length - 1) {
                whereClause += " OR ";
            }
        }

        String cQ = String.format("""
            SELECT tbl_course_data.course_data_id, tbl_course_data.course_title , sum(tbl_word_course_data.count) as course_rank

            FROM tbl_course_data 
            INNER JOIN tbl_word_course_data USING (course_data_id)
            INNER JOIN tbl_course USING (course_id)
            INNER JOIN tbl_word using (word_id)

            WHERE (%s) AND tbl_course.term_id = :termId

            GROUP BY tbl_course_data.course_data_id 

            ORDER BY course_rank DESC

          	LIMIT :limit OFFSET :offset
        """, whereClause);

        Query query = session.createNativeQuery(cQ);

        for (int i = 0; i < words.length; i++) {
            query.setParameter("wordStart" + i, "%" + words[i]);
            query.setParameter("wordEnd" + i, words[i] + "%");
        }


		int pageOffset = resultsPerPage * (page - 1);	

		//* Setting of named HQL placeholders here */

        query.setParameter("termId", termId);
		query.setParameter("limit", resultsPerPage);
		query.setParameter("offset", pageOffset);

        List<Object[]> results = query.getResultList();

		for (Object[] result : results) {

			//Get matching course_data_id

			CourseData courseData = (CourseData) session.createQuery("FROM CourseData WHERE courseDataId = :cid", CourseData.class)
				.setParameter("cid", result[0])
				.uniqueResult();

			//Build a CDR object from it

				CourseDataResult cdr = loadSingleCDR(courseData, session);
				CDR_Query.add(cdr);

				System.out.println("course_data_id: " + result[0]);
				System.out.println("course_title: " + result[1]);
				System.out.println("course_rank: " + result[2]);
				System.out.println();
		}
    }

    return CDR_Query;
}

	
	public static List<CourseDataResult> searchFuzzy(String searchTerm, int page, int resultsPerPage, int termId)
	{
		// Look up in the word index
		ArrayList<CourseData> relevantCDs = new ArrayList<>();

		if (searchTerm == null)
			return new ArrayList<>();

		// Calculate the offset based on the page number
		int pageoffset = resultsPerPage * (page - 1);

		try (Session session = HibernateUtil.getSessionFactory().openSession())
		{
			Arrays.stream(searchTerm.split("\\s+"))
				.map(StringHelper::cleanWord)
				.filter(word -> !word.isEmpty() && word.length() >= 3)
				.forEach(word -> {
						List<Word> matchingWordList = session
								.createQuery("FROM Word w WHERE CONCAT('%', w.word, '%') LIKE :targetWord", Word.class)
								.setParameter("targetWord", "%" + word + "%").getResultList();

						for (Word matchingWord : matchingWordList)
						{
							Logger.debug("Found WORD: {} ID: {}", matchingWord.getWordString(), matchingWord.getId());

							List<WordMap> matchingEntries = session
									.createQuery(
											"FROM WordMap wm WHERE wm.word = :targetId "
													+ "AND wm.courseData.course.term.termId = :termId",
											WordMap.class
									).setParameter("targetId", matchingWord).setParameter("termId", termId)
									// .setFirstResult(pageoffset)
									// .setMaxResults(resultsPerPage)
									.list();

							rank(matchingEntries, relevantCDs);
						}
					});

			return loadIn(relevantCDs, session);
		}
	}

	public static List<CourseDataResult> searchExactWords(String searchTerm, int page, int resultsPerPage, int termId)
	{
		// Look up in the word index
		ArrayList<CourseData> relevantCDs = new ArrayList<>();

		if (searchTerm == null)
			return new ArrayList<>();

		// Calculate the offset based on the page number
		int pageoffset = resultsPerPage * (page - 1);

		try (Session session = HibernateUtil.getSessionFactory().openSession())
		{
			Arrays.stream(searchTerm.split("\\s+")).map(StringHelper::cleanWord).filter(word -> !word.isEmpty())
					.forEach(word -> {
						Word matchingWord = session.createQuery("FROM Word w WHERE w.word = :targetWord", Word.class)
								.setParameter("targetWord", word).uniqueResult();

						if (matchingWord == null)
						{
							return;
						}

						Logger.debug("WORD: {} ID: {}", matchingWord.getWordString(), matchingWord.getId());

						List<WordMap> matchingEntries = session
								.createQuery(
										"FROM WordMap wm WHERE wm.word = :targetId "
												+ "AND wm.courseData.course.term.termId = :termId",
										WordMap.class
								).setParameter("targetId", matchingWord).setParameter("termId", termId)
								.setFirstResult(pageoffset).setMaxResults(resultsPerPage).list();

						rank(matchingEntries, relevantCDs);
					});

			return loadIn(relevantCDs, session);
		}
	}

}
