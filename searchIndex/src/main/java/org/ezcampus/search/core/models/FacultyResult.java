package org.ezcampus.search.core.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FacultyResult
{
	private String instructorName;
	private String instructorEmail;

	public FacultyResult(String name, String email)
	{
		this.instructorEmail = email;
		this.instructorName = name;
	}

	@JsonProperty("instructor_name")
	public String getInstructorName()
	{
		return this.instructorName;
	}

	public void setInstructorName(String value)
	{
		this.instructorName = value;
	}

	@JsonProperty("instructor_email")
	public String getInstructorEmail()
	{
		return this.instructorName;
	}

	public void setInstructorEmail(String value)
	{
		this.instructorEmail = value;
	}
}
