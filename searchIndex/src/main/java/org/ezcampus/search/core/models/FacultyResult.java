package org.ezcampus.search.core.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FacultyResult
{
	private String instructorName;
	private String instrucotorEmail;

	public FacultyResult(String name, String email)
	{
		this.instrucotorEmail = email;
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
		return this.instrucotorEmail;
	}

	public void setInstructorEmail(String value)
	{
		this.instrucotorEmail = value;
	}
}
