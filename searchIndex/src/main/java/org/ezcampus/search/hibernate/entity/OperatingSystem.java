package org.ezcampus.search.hibernate.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="tbl_operating_system")
public class OperatingSystem
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "os_id")
	private Integer osId;

	@Column(name = "os_name", length = 128)
	private String osName;

	public OperatingSystem() {}

	public Integer getOsId() {
		return this.osId;
	}

	public String getOsName() {
		return this.osName;
	}
}
