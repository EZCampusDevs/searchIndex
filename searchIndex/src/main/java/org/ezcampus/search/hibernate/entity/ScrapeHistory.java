package org.ezcampus.search.hibernate.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tbl_scrape_history")
public class ScrapeHistory
{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "scrape_id")
	private Integer scrapeId;

	@Column(name = "scrape_time")
	private Date scrapeTime;

	@Column(name = "has_been_indexed")
	private Boolean hasBeenIndexed;

	public Integer getScrapeId()
	{
		return scrapeId;
	}

	public void setScrapeId(Integer scrapeId)
	{
		this.scrapeId = scrapeId;
	}

	public Date getScrapeTime()
	{
		return scrapeTime;
	}

	public void setScrapeTime(Date scrapeTime)
	{
		this.scrapeTime = scrapeTime;
	}

	public Boolean getHasBeenIndexed()
	{
		if(this.hasBeenIndexed == null) return false;
		return this.hasBeenIndexed;
	}

	public void setHasBeenIndexed(Boolean b)
	{
		this.hasBeenIndexed = b;
	}

	public String toString()
	{
		return String.format("[ScrapeHistory: %d %s]", getScrapeId(), getScrapeTime().toString());
	}
}
