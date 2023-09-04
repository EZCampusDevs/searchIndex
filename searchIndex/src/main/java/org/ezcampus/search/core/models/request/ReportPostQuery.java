package org.ezcampus.search.core.models.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReportPostQuery {

    @JsonProperty("os_id")
    private int osId;

    @JsonProperty("report_type_id")
    private int reportTypeId;

    @JsonProperty("browser_type_id")
    private int browserTypeId;

    @JsonProperty("description")
    private String description;

    // *** Getters and Setters

    public int getOsId() {
        return this.osId;
    }

    public int getReportTypeId() {
        return this.reportTypeId;
    }

    public int getBrowserTypeId() {
        return this.browserTypeId;
    }

    public String getDescription() {
        return this.description;
    }

}