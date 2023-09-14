package org.ezcampus.search.core.models.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StatusResponse
{
	@JsonProperty("is_processing")
	public boolean isProcessing;
	
	@JsonProperty("started_at")
	public long startTime;
	
	@JsonProperty("elapsed_time")
	public long elapsedTime;
	
	@JsonProperty("elapsed_time_pretty")
	public String elapsedTimePretty;
}
