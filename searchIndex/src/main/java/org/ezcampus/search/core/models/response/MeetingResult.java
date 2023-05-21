package org.ezcampus.search.core.models.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MeetingResult {

	private String building;
	private String building_description;
	
	public MeetingResult(String b, String bd) {
		this.building = b;
		this.building_description = bd;
	}

	@JsonProperty("building")
	public String getBuilding() {
		return this.building;
	}
	
	@JsonProperty("building_description")
	public String getBuildingDesc() {
		return this.building_description;
	}
	
}
