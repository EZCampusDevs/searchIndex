package org.ezcampus.search.core;

import java.util.ArrayList;
import java.util.List;

import org.ezcampus.search.hibernate.entity.Course;
import org.ezcampus.search.hibernate.entity.CourseData;
import org.ezcampus.search.hibernate.entity.CourseFaculty;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CourseDataResult {
	
	//DB reference params
	public int course_data_id;
	public int course_id;
	
	//General Information for API response
	
	public String course_code;
	public String course_crn;
	public String course_desc;
	public String class_type;
	
	public ArrayList<String> instructors = new ArrayList();
	public ArrayList<String> instructors_email = new ArrayList();
	
	// Pagination & Display Order 
	
	public int ranking;
	
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
	
	
	public CourseDataResult(Course course, CourseData courseData, List<CourseFaculty> courseFac) {
		
		this.course = course;
		this.courseData = courseData;
		this.courseFac = courseFac;
		
		course_data_id = courseData.getCourseDataId();
		course_id = course.getCourseId();
		
		
		course_code = course.getCourseCode();
		course_crn =courseData.getCrn();
		course_desc = course.getCourseDescription();
		class_type = courseData.getClassType();
		
		ranking = courseData.ranking;
		
		for(CourseFaculty e : courseFac) {
			instructors.add(e.getFaculty().getInstructorName());
			instructors_email.add(e.getFaculty().getInstructorEmail());
		}
		
	} // CDR
	
    @JsonProperty("course_data_id")
    public int getCourseDataId() {
        return course_data_id;
    }

    @JsonProperty("course_id")
    public int getCourseId() {
        return course_id;
    }

    @JsonProperty("course_code")
    public String getCourseCode() {
        return course_code;
    }

    @JsonProperty("course_crn")
    public String getCourseCrn() {
        return course_crn;
    }

    @JsonProperty("course_desc")
    public String getCourseDesc() {
        return course_desc;
    }

    @JsonProperty("class_type")
    public String getClassType() {
        return class_type;
    }

    @JsonProperty("instructors")
    public ArrayList<String> getInstructors() {
        return instructors;
    }

    @JsonProperty("instructors_email")
    public ArrayList<String> getInstructorsEmail() {
        return instructors_email;
    }

    @JsonProperty("ranking")
    public int getRanking() {
        return ranking;
    }
	
}
