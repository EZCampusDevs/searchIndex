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
public class EndpointSearch
{
	private final ObjectMapper jsonMap = new ObjectMapper();

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRequestBody(String jsonPayload)
	{
		Logger.info("got json payload for search");
		
		SearchQuery requestData;
		try
		{
			requestData = jsonMap.readValue(jsonPayload, SearchQuery.class);
		}
		catch (IOException e)
		{
			Logger.debug("Got bad json: {}", e);
			return Response.status(Response.Status.BAD_REQUEST).entity("Invalid JSON payload").build();
		}

		Logger.info("got search terms {}", requestData.getSearchTerm());
		
		List<CourseDataResult> results;

		if (requestData.getSearchMethod())
		{
			Logger.info("searching fuzzy");
			
			results = SearchHandler.searchFuzzy(
					requestData.getSearchTerm(), requestData.getPage(), requestData.getResultsPerPage(),
					requestData.getTerm()
			);
		}
		else
		{ // ⚡ Performance Boosted by: Minno ⚡
			Logger.info("searching native");
			
			results = SearchHandler.searchNative(
					requestData.getSearchTerm(), requestData.getPage(), requestData.getResultsPerPage(),
					requestData.getTerm()
			);
		}

		Logger.info("search results {}", results);

		try
		{
			String jsonArray = jsonMap.writeValueAsString(results);
			return Response.status(Response.Status.OK).entity(jsonArray).build();
		}
		catch (JsonProcessingException e)
		{
			Logger.error(e);
			e.printStackTrace();
		}

		return Response.status(Response.Status.NOT_FOUND).entity("not found").build();
	}
}
