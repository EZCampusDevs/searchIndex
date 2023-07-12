package org.ezcampus.search.api;

import java.io.IOException;
import java.util.List;

//Models
import org.ezcampus.search.core.models.request.TermsQuery;
//Hibernate Entities:
import org.ezcampus.search.hibernate.entity.Term;
import org.ezcampus.search.hibernate.entityDAO.SchoolDAO;
import org.tinylog.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
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

	@Path("term")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRequestBody(String jsonPayload)
	{
		TermsQuery requestData;
		try
		{
			requestData = jsonMap.readValue(jsonPayload, TermsQuery.class);
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