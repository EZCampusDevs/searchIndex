package org.ezcampus.search.hibernate.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "tbl_course_data", uniqueConstraints = @UniqueConstraint(columnNames = { "course_id", "crn" }))
public class CourseData {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "course_data_id")
	private Integer courseDataId;

	@Column(name = "crn")
	private String crn;

	@Column(name = "id")
	private Integer id;

	@Column(name = "course_title")
	private String courseTitle;

	@Column(name = "subject")
	private String subject;

	@Column(name = "subject_long")
	private String subjectLong;

	@Column(name = "sequence_number")
	private String sequenceNumber;

	@Column(name = "campus_description")
	private String campusDescription;

	@Column(name = "credit_hours")
	private Integer creditHours;

	@Column(name = "maximum_enrollment")
	private Integer maximumEnrollment;

	@Column(name = "enrollment")
	private Integer enrollment;

	@Column(name = "seats_vailable")
	private Integer seatsAvailable;

	@Column(name = "wait_capacity")
	private Integer waitCapacity;

	@Column(name = "wait_count")
	private Integer waitCount;

	@Column(name = "wait_available")
	private Integer waitAvailable;

	@Column(name = "credit_hour_high")
	private Integer creditHourHigh;

	@Column(name = "credit_hour_low")
	private Integer creditHourLow;

	@Column(name = "open_section")
	private Boolean openSection;

	@Column(name = "link_identifier")
	private String linkIdentifier;

	@Column(name = "is_section_linked")
	private Boolean isSectionLinked;

	@Column(name = "instructional_method")
	private String instructionalMethod;

	@Column(name = "instructional_method_description")
	private String instructionalMethodDescription;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "course_id", referencedColumnName = "course_id")
	private Course course;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "class_type_id", referencedColumnName = "class_type_id")
	private ClassType classType;

	
    @JoinColumn(name = "scrape_id", referencedColumnName = "scrape_id")
    @ManyToOne(fetch=FetchType.LAZY)
    private ScrapeHistory scrapeId;
   
    
    // Transient makes it invisible to the ORM, but still can be used in instance
    
    @Transient
    public int ranking = 1;
    
	
	public Integer getCourseDataId() {
		return courseDataId;
	}
	
	public Course getCourse() {
		return course;
	}

	public void setCourseDataId(Integer courseDataId) {
		this.courseDataId = courseDataId;
	}

	public String getCrn() {
		return crn;
	}

	public void setCrn(String crn) {
		this.crn = crn;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCourseTitle() {
		return courseTitle;
	}

	public void setCourseTitle(String courseTitle) {
		this.courseTitle = courseTitle;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getSubjectLong() {
		return subjectLong;
	}

	public void setSubjectLong(String subjectLong) {
		this.subjectLong = subjectLong;
	}

	public String getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(String sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public String getCampusDescription() {
		return campusDescription;
	}

	public void setCampusDescription(String campusDescription) {
		this.campusDescription = campusDescription;
	}
	
	public String getInstructionalMethodDescription() {
		return this.instructionalMethodDescription;
	}

	public Integer getCreditHours() {
		return creditHours;
	}
	
	public String getClassType() {
		return this.classType.getClassType();
	}
}