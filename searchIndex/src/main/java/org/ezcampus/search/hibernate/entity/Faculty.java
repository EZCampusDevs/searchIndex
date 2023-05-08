package org.ezcampus.search.hibernate.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;

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

    // Getters and setters
    // ...
}
