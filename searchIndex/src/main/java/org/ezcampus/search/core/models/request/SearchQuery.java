package org.ezcampus.search.core.models.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SearchQuery {
    @JsonProperty("search_term")
    private String searchTerm;

    @JsonProperty("page")
    private int page;

    @JsonProperty("results_per_page")
    private int resultsPerPage;

    @JsonProperty("term_id")
    private int termId;
    
    @JsonProperty("search_method")
    private boolean method;
    
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
    
    public int getTerm() {
        return termId;
    }

    public void setTerm(int t) {
        this.termId = t;
    }
    
    public boolean getSearchMethod() {
    	return this.method;
    }
}
