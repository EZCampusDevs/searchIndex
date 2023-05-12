package org.ezcampus.search.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.ezcampus.search.hibernate.entity.Course;
import org.ezcampus.search.hibernate.entity.CourseData;
import org.ezcampus.search.hibernate.entity.CourseFaculty;
import org.ezcampus.search.hibernate.entity.Faculty;
import org.ezcampus.search.hibernate.entity.Meeting;
import org.ezcampus.search.hibernate.entity.Term;
import org.ezcampus.search.hibernate.entity.WordMap;
import org.ezcampus.search.hibernate.util.HibernateUtil;
import org.ezcampus.search.hibernate.util.WordSearchBuilder;
import org.hibernate.Session;

public class ReverseIndexGen
{

//	public static void main1(String[] args)
//	{
//		ResourceLoader.loadTinyLogConfig();
//
//		System.out.println("Running Reverse Index Gen: ");
//
//		// Testing the DAO
//		System.out.println(WordDAO.getWord("12345"));
//
//		
//		int i = WordDAO.insertWord("hello");
//		
//		Logger.info("inserted word, id was {}", i);
//		
//		
//		DatabaseProcessing.processLastScrape();
//		
//		ScrapeHistory s = ScrapeHistoryDAO.getNewestScrapeHistory();
//
//		if (s == null)
//		{
//			Logger.info("Got NULL");
//		}
//		else
//		{
//			Logger.info(s.toString());
//		}
//
//	}

	public static void main(String[] args)
	{
		// Step 1.

		// Open Hibernate Session
		try (Session session = HibernateUtil.getSessionFactory().openSession())
		{

			// Get all Terms, Class types & courses:

			List<Term> terms = session.createQuery("From Term", Term.class).list();
			List<Course> courses = session.createQuery("FROM Course", Course.class).list();

			// For all Courses, get Course_data_ids
			for (Course course : courses)
			{
				ArrayList<String> searchStrings = new ArrayList<>(); //List of attributed words
				
				int id = course.getCourseId();

				// Make queries for course_data id
				List<CourseData> courseDatas = session
						.createQuery("FROM CourseData cd WHERE cd.course.courseId = :id", CourseData.class)
						.setParameter("id", id).list();

				// Course Code
				String cc = course.getCourseCode();

				for (CourseData courseData : courseDatas)
				{
					
					// 1.) Getting Professor names:
					
					List<CourseFaculty> cfl = session
							.createQuery("From CourseFaculty c WHERE c.courseDataId = :cId", CourseFaculty.class)
							.setParameter("cId", courseData).list();
					
					//Essentially adding all Instructors teaching a specific course_data_id, A.K.A CRN
					
					for(CourseFaculty cF : cfl) {
						
						Faculty facId = cF.getFaculty();
						searchStrings.add(facId.getInstructorName());
							
					}

					// CRN or Course Number
					String crn = courseData.getCrn();
					// Class Type (LEC, LAB, TUT)
					String classType = courseData.getClassType();


					// Meeting Location(s) -> All meetings with matching course data id

					List<Meeting> meetings = session
							.createQuery("FROM Meeting m WHERE m.courseDataId = :cId", Meeting.class)
							.setParameter("cId", courseData).list();

					// Put all unique search strings into searchWords

					for (Meeting meeting : meetings)
					{

						String building = meeting.getBuildingDescription();

						if (!searchStrings.contains(building) && building != null)
						{
							searchStrings.add(building);
						}
					}

					// Course Code
					searchStrings.add(course.getCourseCode());
					// CRN or Course Number
					searchStrings.add(courseData.getCrn());
					// Class Type (LEC, LAB, TUT)
					searchStrings.add(courseData.getClassType());


					WordSearchBuilder build = new WordSearchBuilder(courseData, searchStrings, session);

					System.out.println(courseData.getCourseDataId());
				}
			}
		}
		
		System.out.println("Word & Word Map | Table Loaded-in Completed");
	}
}
