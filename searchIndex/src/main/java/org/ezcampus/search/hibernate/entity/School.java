package org.ezcampus.search.hibernate.entity;


import com.fasterxml.jackson.annotation.JsonGetter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tbl_school")
public class School {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "school_id")
    private Integer schoolId;

    @Column(name = "school_unique_value", length = 128)
    private String schoolUniqueValue;
    
    @Column(name = "subdomain", length = 64)
    private String subdomain;
    
    @Column(name = "timezone", length = 64)
    private String timezone;

    
    @JsonGetter
	public Integer getSchoolId()
	{
		return schoolId;
	}

	public void setSchoolId(Integer schoolId)
	{
		this.schoolId = schoolId;
	}

	@JsonGetter
	public String getSchoolUniqueValue()
	{
		return schoolUniqueValue;
	}

	public void setSchoolUniqueValue(String schoolUniqueValue)
	{
		this.schoolUniqueValue = schoolUniqueValue;
	}

	@JsonGetter
	public String getSubdomain()
	{
		return subdomain;
	}

	public void setSubdomain(String subdomain)
	{
		this.subdomain = subdomain;
	}

	@JsonGetter
	public String getTimezone()
	{
		return timezone;
	}

	public void setTimezone(String timezone)
	{
		this.timezone = timezone;
	}
}
