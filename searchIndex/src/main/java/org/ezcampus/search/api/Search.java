package org.ezcampus.search.api;

import java.io.IOException;
import java.util.List;

import org.ezcampus.search.core.SearchHandler;
import org.ezcampus.search.core.models.request.SearchQuery;
import org.ezcampus.search.core.models.response.CourseDataResult;
import org.ezcampus.search.hibernate.util.HibernateUtil;
import org.hibernate.Session;
import org.tinylog.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

//Hibernate Entities:
import org.ezcampus.search.hibernate.entity.Term;
import org.ezcampus.search.hibernate.entity.Word;


@Path("/search")
public class Search
{
	private final ObjectMapper jsonMap = new ObjectMapper();

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRequestBody(String jsonPayload) {
		try {

			SearchQuery requestData = jsonMap.readValue(jsonPayload, SearchQuery.class);

			List<CourseDataResult> results = SearchHandler.searchFuzzy(
					requestData.getSearchTerm(), 
					requestData.getPage(), 
					requestData.getResultsPerPage(),
					requestData.getTerm()
			);

			// Convert to JSON array string
			try {
				String jsonArray = jsonMap.writeValueAsString(results);

				return Response.status(Response.Status.OK).entity(jsonArray).build(); // Response with 200 OK and data
																						// (search results)

			} catch (JsonProcessingException e) { //Error handling for JSON building of results
				e.printStackTrace();
			}

			return Response.status(Response.Status.NOT_FOUND).entity("not found").build();
		} catch (IOException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Invalid JSON payload").build();
		}
	}

	@GET
	@Path("orm")
	@Produces(MediaType.TEXT_PLAIN)
	public Response getRequestBody2(
			@QueryParam("search_term") String searchTerm, @QueryParam("page") int page,
			@QueryParam("results_per_page") int resultsPerPage
	)
	{

		Logger.debug("search function starting");

		try (Session session = HibernateUtil.getSessionFactory().openSession())
		{
			Logger.debug("In session try statement");

			List<Word> existingWord = session.createQuery("FROM Word ", Word.class).getResultList();

			existingWord.forEach(x -> {
				Logger.info(x);
			});

			String a = String.join(", ", existingWord.stream().map(x -> x.getWordString()).toList());

			return Response.status(Response.Status.OK).entity(a).build();
		}

//		return Response.status(Response.Status.NOT_FOUND).entity("not found").build();

	}
        
        //Import all in Netbeans: CTRL + SHIFT + I
        
    @GET
    @Path("dummy")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getRequestDummy(@QueryParam("p") String p){
    
        try (Session session = HibernateUtil.getSessionFactory().openSession() )
        {
        
        List<Term> existingTerms = session.createQuery("FROM Term", Term.class).getResultList();
        String str = String.join(", ", existingTerms.stream().map(x -> x.toString()).toList());
        str += " ... oh yea, and your word is: "+p;

        return Response.status(Response.Status.OK).entity(str).build();
        
        }


    }
        
}

