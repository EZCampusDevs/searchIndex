package org.ezcampus.search.hibernate.entity;

import java.util.Date;

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
@Table(name = "tbl_report")
public class Report
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "report_id")
	private Integer reportId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "report_type")
	private ReportType reportType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "operating_system")
	private OperatingSystem operatingSystem;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "browser_description")
	private Browser browser;
	
	@Column(name = "created_at")
	private Date createdAt;
	
	@Column(name = "description")
	private String description;
}
