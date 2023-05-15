package org.ezcampus.search.hibernate.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "tbl_course_faculty")
public class CourseFaculty {

	  @Id
	    @ManyToOne(fetch=FetchType.LAZY)
	    @JoinColumn(name = "course_data_id", referencedColumnName = "course_data_id")
	    private CourseData courseDataId;

	    @Id
	    @ManyToOne(fetch=FetchType.LAZY)
	    @JoinColumn(name = "faculty_id", referencedColumnName = "faculty_id")
	    private Faculty faculty;
	    
	    // getters and setters omitted for brevity
	    public CourseData getCourseDataId() {
	        return this.courseDataId;
	    }

	    public void setCourseDataId(CourseData courseDataId) {
	        this.courseDataId = courseDataId;
	    }

	    public Faculty getFaculty() {
	        return this.faculty;
	    }

	    public void setFacultyId(Faculty facultyId) {
	        this.faculty = facultyId;
	    }

}
