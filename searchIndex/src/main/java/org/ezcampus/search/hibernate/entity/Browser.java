package org.ezcampus.search.hibernate.entity;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="tbl_browser")
public class Browser
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "browser_id")
	private Integer browserId;

	@Column(name = "browser_name", length = 128)
	private String browserName;
	
	@Column(name = "browser_version", length = 128)
	private String browserVersion;
}
