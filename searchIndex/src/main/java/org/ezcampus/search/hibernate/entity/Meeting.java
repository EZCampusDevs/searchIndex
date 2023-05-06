package org.ezcampus.search.hibernate.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

    @ManyToOne
    @JoinColumn(name = "course_data_id")
    private CourseData courseDataId;

    @ManyToOne
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

    // Getters and setters omitted for brevity
    public String getBuildingDescription() {
        return this.buildingDescription;
    }

}

