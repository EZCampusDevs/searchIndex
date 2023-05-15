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

@Entity
@Table(name = "tbl_faculty")
public class Faculty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "faculty_id")
    private Integer facultyId;

    @Column(name = "banner_id", unique = true, columnDefinition = "BINARY(32)")
    private byte[] bannerId;


    @Column(name = "instructor_name", length = 128)
    private String instructorName;

    @Column(name = "instructor_email", length = 128)
    private String instructorEmail;

    @Column(name = "instructor_rating")
    private Integer instructorRating;

    @JoinColumn(name = "scrape_id", referencedColumnName = "scrape_id")
    @ManyToOne(fetch=FetchType.LAZY)
    private ScrapeHistory scrapeId;
    
    public Integer getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(Integer facultyId) {
        this.facultyId = facultyId;
    }

    public byte[] getBannerId() {
        return bannerId;
    }

    public void setBannerId(byte[] bannerId) {
        this.bannerId = bannerId;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public String getInstructorEmail() {
        return instructorEmail;
    }

    public void setInstructorEmail(String instructorEmail) {
        this.instructorEmail = instructorEmail;
    }

    public Integer getInstructorRating() {
        return instructorRating;
    }

    public void setInstructorRating(Integer instructorRating) {
        this.instructorRating = instructorRating;
    }

    public ScrapeHistory getScrapeId() {
        return scrapeId;
    }

    public void setScrapeId(ScrapeHistory scrapeId) {
        this.scrapeId = scrapeId;
    }

}
