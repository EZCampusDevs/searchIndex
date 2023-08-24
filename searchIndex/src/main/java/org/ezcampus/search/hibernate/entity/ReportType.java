package org.ezcampus.search.hibernate.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tbl_report_type")
public class ReportType
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "report_type_id")
	private Integer reportTypeId;

	@Column(name = "report_type_name", length = 128)
	private String reportTypeName;
	
	@Column(name = "report_type_description", length = 512)
	private String reportTypeDescription;
}
