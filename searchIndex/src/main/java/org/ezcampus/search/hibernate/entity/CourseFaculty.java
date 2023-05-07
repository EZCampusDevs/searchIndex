package org.ezcampus.search.hibernate.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

	//Many Profs can teach 1 CRN ; Many Course_Data_Id's in Course Faculty that map's to 1 Course_data row
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_faculty_id")
    private Integer courseFacultyId;
	
    @ManyToOne
    @JoinColumn(name = "course_data_id")
    private CourseData courseDataId;


    @ManyToOne
    @JoinColumn(name = "faculty_id", referencedColumnName = "faculty_id", insertable = false)
    private Faculty faculty;

}
