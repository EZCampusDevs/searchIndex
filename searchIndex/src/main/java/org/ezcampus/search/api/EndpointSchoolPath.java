package org.ezcampus.search.api;

import java.io.IOException;
import java.util.List;

//Models
import org.ezcampus.search.core.models.request.SchoolQuery;
import org.ezcampus.search.hibernate.entity.School;
//Hibernate Entities:
import org.ezcampus.search.hibernate.entity.Term;
import org.ezcampus.search.hibernate.entityDAO.SchoolDAO;
import org.tinylog.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

// Nice Boilerplate with Assertion for bad Post Payloads
// @POST
// @Consumes(MediaType.APPLICATION_JSON)
// @Produces(MediaType.APPLICATION_JSON)
// public Response getRequestBody(String jsonPayload) {
//     try {

//     // Jackson parse jsonPayloadh ere

//     } catch (IOException e) {
//             return Response.status(Response.Status.BAD_REQUEST).entity("Invalid JSON payload").build();
//     }

// }

@Path("/school")
public class EndpointSchoolPath
{
	private final ObjectMapper jsonMap = new ObjectMapper();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSchools() 
	{
		List<School> schools = SchoolDAO.getAllSchools();
		
		try
		{
			String jsonArray = jsonMap.writeValueAsString(schools);
			
			return Response.status(Response.Status.OK)
					.entity(jsonArray)
					.build();
		}
		catch (JsonProcessingException e)
		{
			Logger.error(e);
			e.printStackTrace();
		}
		
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
				.build();
	}
	

	@Path("{school_id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSchoolInfo(@PathParam("school_id") int school_id) 
	{
		if(school_id <= 0) {
			return Response.status(Response.Status.BAD_REQUEST)
					.entity("School Id Cannot be <= 0")
					.build();
		}
		
		School s = SchoolDAO.schoolFromID(school_id);
		
		Logger.debug(s);
		
		if(s == null) {
			return Response.status(Response.Status.NOT_FOUND)
					.build();
		}
		
		Logger.debug("School {}, id {} value {}", s, s.getSchoolId(), s.getSchoolUniqueValue());
		
		try
		{
			String jsonArray = jsonMap.writeValueAsString(s);
			
			return Response.status(Response.Status.OK)
					.entity(jsonArray)
					.build();
		}
		catch (JsonProcessingException e)
		{
			Logger.error(e);
			e.printStackTrace();
		}
		
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
				.build();
		
	}
	
	@Path("term")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRequestBody(String jsonPayload)
	{
		SchoolQuery requestData;
		try
		{
			requestData = jsonMap.readValue(jsonPayload, SchoolQuery.class);
		}
		catch (IOException e)
		{
			return Response.status(Response.Status.BAD_REQUEST).entity("Invalid JSON payload").build();
		}

		
		List<Term> SchoolTerms = SchoolDAO.getTerms(requestData);

		try
		{
			String jsonArray = jsonMap.writeValueAsString(SchoolTerms);
			return Response.status(Response.Status.OK).entity(jsonArray).build();
		}
		catch (JsonProcessingException e)
		{
			Logger.error(e);
			e.printStackTrace();
		}

		return Response.status(Response.Status.NOT_FOUND).entity("404 not found...").build();
	}
}