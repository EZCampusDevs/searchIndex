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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

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
	
	@Column(name = "created_at", columnDefinition = "TIMESTAMP default CURRENT_TIMESTAMP")
	private Date createdAt;
	
	@Column(name = "description")
	private String description;

    public Report() {
        this.createdAt = new Date();
    }

	public void setBrowser(Browser browser) {
		this.browser = browser;
	}

	public void setOperatingSystem(OperatingSystem operatingSystem) {
		this.operatingSystem = operatingSystem;
	}

	public void setReportType(ReportType reportType) {
		this.reportType = reportType;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
