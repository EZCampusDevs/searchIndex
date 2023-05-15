package org.ezcampus.search.core.models;

import java.util.ArrayList;
import java.util.List;

import org.ezcampus.search.hibernate.entity.Course;
import org.ezcampus.search.hibernate.entity.CourseData;
import org.ezcampus.search.hibernate.entity.CourseFaculty;
import org.tinylog.Logger;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CourseDataResult
{
	// DB reference params
	private int course_data_id;
	private int course_id;

	// General Information for API response
	private String course_code;
	private String course_crn;
	private String course_desc;
	private String class_type;

	private List<FacultyResult> instructors;

	// Pagination & Display Order
	private int ranking;

	public CourseDataResult(Course course, CourseData courseData, List<CourseFaculty> courseFac)
	{
		course_data_id = courseData.getCourseDataId();
		course_id = course.getCourseId();

		course_code = course.getCourseCode();
		course_crn = courseData.getCrn();
		course_desc = course.getCourseDescription();
		class_type = courseData.getClassType();

		ranking = courseData.ranking;

		this.instructors = new ArrayList<>(courseFac.size());

		for (CourseFaculty e : courseFac)
		{
			instructors.add(new FacultyResult(
					e.getFaculty().getInstructorName(), 
					e.getFaculty().getInstructorEmail()));
		}

	} 

	public void Print()
	{
		Logger.info("*****************************************");

		Logger.info(">> course_data_id: {}", course_data_id);
		Logger.info(">> course_id: {}", course_id);

		Logger.info("CourseCode: {}", course_code);
		Logger.info("CRN: {}", course_crn);
		Logger.info("Description: {}", course_desc);
		Logger.info("ClassType: {}", class_type);

		for (FacultyResult e : instructors)
		{
			Logger.info("Prof: {}", e.getInstructorName());
			Logger.info("Email: {}", e.getInstructorEmail());

		}

		Logger.info("Ranking: {}", ranking);

		Logger.info("*****************************************\n");
	}

	
	@JsonProperty("course_data_id")
	public int getCourseDataId()
	{
		return course_data_id;
	}

	@JsonProperty("course_id")
	public int getCourseId()
	{
		return course_id;
	}

	@JsonProperty("course_code")
	public String getCourseCode()
	{
		return course_code;
	}

	@JsonProperty("course_crn")
	public String getCourseCrn()
	{
		return course_crn;
	}

	@JsonProperty("course_desc")
	public String getCourseDesc()
	{
		return course_desc;
	}

	@JsonProperty("class_type")
	public String getClassType()
	{
		return class_type;
	}

	@JsonProperty("instructors")
	public List<FacultyResult> getInstructors()
	{
		return instructors;
	}

	@JsonProperty("ranking")
	public int getRanking()
	{
		return ranking;
	}

	public void setCourse_data_id(int course_data_id)
	{
		this.course_data_id = course_data_id;
	}

	public void setCourse_id(int course_id)
	{
		this.course_id = course_id;
	}

	public void setCourse_code(String course_code)
	{
		this.course_code = course_code;
	}

	public void setCourse_crn(String course_crn)
	{
		this.course_crn = course_crn;
	}

	public void setCourse_desc(String course_desc)
	{
		this.course_desc = course_desc;
	}

	public void setClass_type(String class_type)
	{
		this.class_type = class_type;
	}

	public void setInstructors(List<FacultyResult> instructors)
	{
		this.instructors = instructors;
	}

	public void setRanking(int ranking)
	{
		this.ranking = ranking;
	}
	
	
	public String toString() {
		return String.format("<CourseDataResult: %s>", this.course_code);
	}
}
