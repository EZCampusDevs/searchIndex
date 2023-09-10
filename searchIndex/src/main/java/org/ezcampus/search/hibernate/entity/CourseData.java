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

	@Column(name = "course_title")
	private String courseTitle;

	@Column(name = "sequence_number")
	private String sequenceNumber;

	@Column(name = "campus_description")
	private String campusDescription;

	@Column(name = "credit_hours")
	private Integer creditHours;

	@Column(name = "maximum_enrollment")
	private Integer maximumEnrollment;

	@Column(name = "current_enrollment")
	private Integer currentEnrollment;

	@Column(name = "maximum_waitlist")
	private Integer maximumWaitlist;

	@Column(name = "current_waitlist")
	private Integer currentWaitlist;

	@Column(name = "open_section")
	private Boolean openSection;

	@Column(name = "link_identifier")
	private String linkIdentifier;

	@Column(name = "is_section_linked")
	private Boolean isSectionLinked;

	@Column(name = "delivery")
	private String delivery;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "course_id", referencedColumnName = "course_id")
	private Course course;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "class_type_id", referencedColumnName = "class_type_id")
	private ClassType classType;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "subject_id", referencedColumnName = "subject_id")
	private Subject subject;

	@JoinColumn(name = "scrape_id", referencedColumnName = "scrape_id")
    @ManyToOne(fetch=FetchType.LAZY)
    private ScrapeHistory scrapeId;
   
	@Column(name = "should_be_indexed")
	private Boolean shouldBeIndexed;
	
    public Integer getCourseDataId()
	{
		return courseDataId;
	}



	public void setCourseDataId(Integer courseDataId)
	{
		this.courseDataId = courseDataId;
	}



	public String getCrn()
	{
		return crn;
	}



	public void setCrn(String crn)
	{
		this.crn = crn;
	}



	public String getCourseTitle()
	{
		return courseTitle;
	}



	public void setCourseTitle(String courseTitle)
	{
		this.courseTitle = courseTitle;
	}



	public String getSequenceNumber()
	{
		return sequenceNumber;
	}



	public void setSequenceNumber(String sequenceNumber)
	{
		this.sequenceNumber = sequenceNumber;
	}



	public String getCampusDescription()
	{
		return campusDescription;
	}



	public void setCampusDescription(String campusDescription)
	{
		this.campusDescription = campusDescription;
	}



	public Integer getCreditHours()
	{
		return creditHours;
	}



	public void setCreditHours(Integer creditHours)
	{
		this.creditHours = creditHours;
	}



	public Integer getMaximumEnrollment()
	{
		return maximumEnrollment;
	}



	public void setMaximumEnrollment(Integer maximumEnrollment)
	{
		this.maximumEnrollment = maximumEnrollment;
	}



	public Integer getCurrentEnrollment()
	{
		return currentEnrollment;
	}



	public void setCurrentEnrollment(Integer currentEnrollment)
	{
		this.currentEnrollment = currentEnrollment;
	}



	public Integer getMaximumWaitlist()
	{
		return maximumWaitlist;
	}



	public void setMaximumWaitlist(Integer maximumWaitlist)
	{
		this.maximumWaitlist = maximumWaitlist;
	}



	public Integer getCurrentWaitlist()
	{
		return currentWaitlist;
	}



	public void setCurrentWaitlist(Integer currentWaitlist)
	{
		this.currentWaitlist = currentWaitlist;
	}



	public Boolean getOpenSection()
	{
		return openSection;
	}



	public void setOpenSection(Boolean openSection)
	{
		this.openSection = openSection;
	}



	public String getLinkIdentifier()
	{
		return linkIdentifier;
	}



	public void setLinkIdentifier(String linkIdentifier)
	{
		this.linkIdentifier = linkIdentifier;
	}



	public Boolean getIsSectionLinked()
	{
		return isSectionLinked;
	}



	public void setIsSectionLinked(Boolean isSectionLinked)
	{
		this.isSectionLinked = isSectionLinked;
	}



	public String getDelivery()
	{
		return delivery;
	}



	public void setDelivery(String delivery)
	{
		this.delivery = delivery;
	}



	public Course getCourse()
	{
		return course;
	}



	public void setCourse(Course course)
	{
		this.course = course;
	}



	public Subject getSubject()
	{
		return subject;
	}



	public void setSubject(Subject subject)
	{
		this.subject = subject;
	}



	public ScrapeHistory getScrapeId()
	{
		return scrapeId;
	}



	public void setScrapeId(ScrapeHistory scrapeId)
	{
		this.scrapeId = scrapeId;
	}



	public void setClassType(ClassType classType)
	{
		this.classType = classType;
	}


    
    // Transient makes it invisible to the ORM, but still can be used in instance
    
    @Transient
    public int ranking = 1;
    

	
	public String getClassType() {
		return this.classType.getClassType();
	}
}