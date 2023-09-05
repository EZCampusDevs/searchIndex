package org.ezcampus.search.core.models.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GenericMessageResult {
    
    private String message;

    public GenericMessageResult(String errorMessage) {
        this.message = errorMessage;
    }

    @JsonProperty("message")
	public String getMessage()
	{
		return this.message;
	}

    public void setMessage(String errorMessage)
	{
		this.message = errorMessage;
	}

}
