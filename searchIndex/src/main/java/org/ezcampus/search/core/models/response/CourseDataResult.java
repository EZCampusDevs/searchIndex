package org.ezcampus.search.core.models.response;

import java.util.ArrayList;
import java.util.List;

import org.ezcampus.search.hibernate.entity.Course;
import org.ezcampus.search.hibernate.entity.CourseData;
import org.ezcampus.search.hibernate.entity.CourseFaculty;
import org.ezcampus.search.hibernate.entity.Meeting;
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
	private String course_title;

	private List<FacultyResult> instructors;
	private List<MeetingResult> extra; 
	
	// Pagination & Display Order
	private int ranking;

	public CourseDataResult(Course course, CourseData courseData,List<Object[]> courseFac_courseMeetings)
	{

		//Assigning entries from `Course`
		
		course_id = course.getCourseId();
		course_code = course.getCourseCode();
		course_desc = course.getCourseDescription();
		
		
		//Assigning entries from `CourseData`
		
		course_data_id = courseData.getCourseDataId();
		course_crn = courseData.getCrn();
		class_type = courseData.getClassType();
		course_title = courseData.getCourseTitle();
		
		
		ranking = courseData.ranking;
		
		//Assigning from list of CourseFaculty (Building FacultyResult objs)

		this.instructors = new ArrayList<>(courseFac_courseMeetings.size());
		this.extra = new ArrayList<>(courseFac_courseMeetings.size());
		
		for (Object[] result : courseFac_courseMeetings)
		{
			Logger.debug("Instructor: {}, Meeting {}", result[0], result[1]);
			
		    CourseFaculty e = (CourseFaculty) result[0];
		    if(e != null)
			instructors.add(new FacultyResult(
					e.getFaculty().getInstructorName(), 
					e.getFaculty().getInstructorEmail()));
		    
			Meeting m = (Meeting) result[1];
			if(m != null)
			extra.add(
					new MeetingResult( m.getBuilding() , 
							m.getBuildingDescription()));
			
		}
		
		Logger.debug("instructor size {}", instructors.size());
		Logger.debug("meeting size {}", extra.size());
	}
	
	public CourseDataResult(Course course, CourseData courseData, List<CourseFaculty> courseFac, List<Meeting> meets)
	{
		
		//Assigning entries from `Course`
		
		course_id = course.getCourseId();
		course_code = course.getCourseCode();
		course_desc = course.getCourseDescription();
		
		
		//Assigning entries from `CourseData`
		
		course_data_id = courseData.getCourseDataId();
		course_crn = courseData.getCrn();
		class_type = courseData.getClassType();
		course_title = courseData.getCourseTitle();
		
		
		ranking = courseData.ranking;
		
		//Assigning from list of CourseFaculty (Building FacultyResult objs)

		this.instructors = new ArrayList<>(courseFac.size());

		for (CourseFaculty e : courseFac)
		{
			instructors.add(new FacultyResult(
					e.getFaculty().getInstructorName(), 
					e.getFaculty().getInstructorEmail()));
		}
		
		//Assigning from list of Meetings

		this.extra = new ArrayList<>(meets.size());
		
		for (Meeting m : meets) 
		{
			
			extra.add(
					new MeetingResult( m.getBuilding() , 
							m.getBuildingDescription()));
			
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
	
	@JsonProperty("course_title")
	public String getCourseTitle()
	{
		return course_title;
	}
	
	//Meeting geters
	
	@JsonProperty("extra")
	public List<MeetingResult> getExtra()
	{
		return extra;
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
