package org.ezcampus.search.hibernate.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tbl_subject")
public class Subject
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "subject_id")
	private int subjectId;

	@Column(name = "subject")
	private String subject;

	@Column(name = "subject_long")
	private String subjectLong;

	public int getSubjectId()
	{
		return subjectId;
	}

	public void setSubjectId(int subjectId)
	{
		this.subjectId = subjectId;
	}

	public String getSubject()
	{
		return subject;
	}

	public void setSubject(String subject)
	{
		this.subject = subject;
	}

	public String getSubjectLong()
	{
		return subjectLong;
	}

	public void setSubjectLong(String subjectLong)
	{
		this.subjectLong = subjectLong;
	}


}
