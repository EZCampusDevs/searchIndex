package org.ezcampus.search.core;

import java.util.ArrayList;
import java.util.List;

import org.ezcampus.search.hibernate.entity.Course;
import org.ezcampus.search.hibernate.entity.CourseData;
import org.ezcampus.search.hibernate.entity.CourseFaculty;
import org.ezcampus.search.hibernate.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class CourseDataResult {
	Course course;
	CourseData courseData;
	List<CourseFaculty> courseFac;

	public void Print() {
		
		System.out.println("*****************************************");
		
		System.out.println(">> course_data_id: "+courseData.getCourseDataId());
		System.out.println(">> course_id: "+course.getCourseId());
		
		System.out.println("CC: "+course.getCourseCode());
		System.out.println("CRN: "+courseData.getCrn());
		System.out.println("Desc: "+course.getCourseDescription());
		System.out.println("CT: "+courseData.getClassType());
		
		for(CourseFaculty e : courseFac) {

			System.out.println("Prof: "+e.getFaculty().getInstructorName());
			System.out.println("Email: "+e.getFaculty().getInstructorEmail());
		
		}
		
		System.out.println("Ranking: " + courseData.ranking);
		
		System.out.println("*****************************************\n");
	}
	
	
	public CourseDataResult(Course c, CourseData cd, List<CourseFaculty> cf) {
		this.course = c;
		this.courseData = cd;
		this.courseFac = cf;
	}
	
}
