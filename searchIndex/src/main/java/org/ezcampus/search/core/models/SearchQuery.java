package org.ezcampus.search.core.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SearchQuery {
    @JsonProperty("search_term")
    private String searchTerm;

    @JsonProperty("page")
    private int page;

    @JsonProperty("results_per_page")
    private int resultsPerPage;

    //*** Getters and Setters
    
    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getResultsPerPage() {
        return resultsPerPage;
    }

    public void setResultsPerPage(int resultsPerPage) {
        this.resultsPerPage = resultsPerPage;
    }
    
}
