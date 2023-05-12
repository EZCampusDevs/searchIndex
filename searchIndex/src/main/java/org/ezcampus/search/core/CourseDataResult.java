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

	
	public CourseDataResult(Course c, CourseData cd, List<CourseFaculty> cf) {
		this.course = c;
		this.courseData = cd;
		this.courseFac = cf;
	}
}
