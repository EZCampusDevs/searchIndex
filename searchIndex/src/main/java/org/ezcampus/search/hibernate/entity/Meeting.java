package org.ezcampus.search.hibernate.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tbl_meeting")
public class Meeting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meeting_id")
    private Integer meetingId;

    @Column(name = "meeting_hash", unique = true)
    private byte[] meetingHash;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "course_data_id")
    private CourseData courseDataId;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "term_id")
    private Term termId;

    @Column(name = "crn")
    private String crn;

    @Column(name = "building")
    private String building;

    @Column(name = "building_description")
    private String buildingDescription;

    @Column(name = "campus")
    private String campus;

    @Column(name = "campus_description")
    private String campusDescription;

    @Column(name = "meeting_type")
    private String meetingType;

    @Column(name = "meeting_type_description")
    private String meetingTypeDescription;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "begin_time")
    private String beginTime;

    @Column(name = "end_time")
    private String endTime;

    @Column(name = "days_of_week")
    private Integer daysOfWeek;

    @Column(name = "room")
    private String room;

    @Column(name = "category")
    private String category;

    @Column(name = "credit_hour_session")
    private Float creditHourSession;

    @Column(name = "hours_week")
    private Float hoursWeek;

    @Column(name = "meeting_schedule_type")
    private String meetingScheduleType;
    
    @Column(name = "time_delta")
    private Integer timeDelta;

	public Integer getMeetingId()
	{
		return meetingId;
	}

	public void setMeetingId(Integer meetingId)
	{
		this.meetingId = meetingId;
	}

	public byte[] getMeetingHash()
	{
		return meetingHash;
	}

	public void setMeetingHash(byte[] meetingHash)
	{
		this.meetingHash = meetingHash;
	}

	public CourseData getCourseDataId()
	{
		return courseDataId;
	}

	public void setCourseDataId(CourseData courseDataId)
	{
		this.courseDataId = courseDataId;
	}

	public Term getTermId()
	{
		return termId;
	}

	public void setTermId(Term termId)
	{
		this.termId = termId;
	}

	public String getCrn()
	{
		return crn;
	}

	public void setCrn(String crn)
	{
		this.crn = crn;
	}

	public String getBuilding()
	{
		return building;
	}

	public void setBuilding(String building)
	{
		this.building = building;
	}

	public String getBuildingDescription()
	{
		return buildingDescription;
	}

	public void setBuildingDescription(String buildingDescription)
	{
		this.buildingDescription = buildingDescription;
	}

	public String getCampus()
	{
		return campus;
	}

	public void setCampus(String campus)
	{
		this.campus = campus;
	}

	public String getCampusDescription()
	{
		return campusDescription;
	}

	public void setCampusDescription(String campusDescription)
	{
		this.campusDescription = campusDescription;
	}

	public String getMeetingType()
	{
		return meetingType;
	}

	public void setMeetingType(String meetingType)
	{
		this.meetingType = meetingType;
	}

	public String getMeetingTypeDescription()
	{
		return meetingTypeDescription;
	}

	public void setMeetingTypeDescription(String meetingTypeDescription)
	{
		this.meetingTypeDescription = meetingTypeDescription;
	}

	public Date getStartDate()
	{
		return startDate;
	}

	public void setStartDate(Date startDate)
	{
		this.startDate = startDate;
	}

	public Date getEndDate()
	{
		return endDate;
	}

	public void setEndDate(Date endDate)
	{
		this.endDate = endDate;
	}

	public String getBeginTime()
	{
		return beginTime;
	}

	public void setBeginTime(String beginTime)
	{
		this.beginTime = beginTime;
	}

	public String getEndTime()
	{
		return endTime;
	}

	public void setEndTime(String endTime)
	{
		this.endTime = endTime;
	}

	public Integer getDaysOfWeek()
	{
		return daysOfWeek;
	}

	public void setDaysOfWeek(Integer daysOfWeek)
	{
		this.daysOfWeek = daysOfWeek;
	}

	public String getRoom()
	{
		return room;
	}

	public void setRoom(String room)
	{
		this.room = room;
	}

	public String getCategory()
	{
		return category;
	}

	public void setCategory(String category)
	{
		this.category = category;
	}

	public Float getCreditHourSession()
	{
		return creditHourSession;
	}

	public void setCreditHourSession(Float creditHourSession)
	{
		this.creditHourSession = creditHourSession;
	}

	public Float getHoursWeek()
	{
		return hoursWeek;
	}

	public void setHoursWeek(Float hoursWeek)
	{
		this.hoursWeek = hoursWeek;
	}

	public String getMeetingScheduleType()
	{
		return meetingScheduleType;
	}

	public void setMeetingScheduleType(String meetingScheduleType)
	{
		this.meetingScheduleType = meetingScheduleType;
	}

	public Integer getTimeDelta()
	{
		return timeDelta;
	}

	public void setTimeDelta(Integer timeDelta)
	{
		this.timeDelta = timeDelta;
	}


}

