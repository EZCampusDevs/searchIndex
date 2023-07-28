package org.ezcampus.search.core.models.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SchoolQuery {

    @JsonProperty("school_name")
    private String schoolName;

    @JsonProperty("school_id")
    private int schoolId;

    //*** Getters and Setters
    
    public String getSchoolName() {
        return this.schoolName;
    }
    
    public int getSchoolId() {
        return this.schoolId;
    }

}