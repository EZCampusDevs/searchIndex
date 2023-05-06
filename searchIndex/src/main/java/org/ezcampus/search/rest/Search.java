package org.ezcampus.search.rest;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

import org.ezcampus.search.hibernate.entity.ClassType;
import org.ezcampus.search.hibernate.entity.Term;
import org.ezcampus.search.hibernate.entity.Word;
import org.ezcampus.search.hibernate.entity.WordDAO;
import org.ezcampus.search.hibernate.util.SessionUtil;
import org.hibernate.Session;

import com.fasterxml.jackson.databind.ObjectMapper;


@Path("/search")
public class Search {
    
    private final ObjectMapper jsonMap = new ObjectMapper();
        
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRequestBody(
            @QueryParam("search_term") String searchTerm,
            @QueryParam("page") int page,
            @QueryParam("results_per_page") int resultsPerPage
    ) 
    {
       try {
    	   return Response.status(Response.Status.OK).entity(
    			   WordDAO.getWord("12345")
    			   ).build();
       } catch (NullPointerException e) {
           return Response.status(Response.Status.NOT_FOUND).entity(
                   "not found"
           ).build();  
       }
    	
    
               
    }
    
    
}
