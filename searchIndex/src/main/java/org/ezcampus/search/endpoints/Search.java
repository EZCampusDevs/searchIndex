package org.ezcampus.search.endpoints;

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
        
		try (Session session = SessionUtil.getSessionFactory().openSession())
		{	
			
			List<Word> words = session.createQuery("FROM Word", Word.class).list();
			for (Word word : words)
			{
				System.out.println(word);
			}
				
		}
    	
    	
        return Response
                .status(200) //Status always first! Builds 200 Response
              //  .entity(jsonString) //Add JSON body
                .build(); 
               
    }
    
    
}
