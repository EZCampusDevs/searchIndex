package org.ezcampus.search.api;

import java.util.HashMap;

import org.ezcampus.search.core.models.response.StatusResponse;
import org.ezcampus.search.data.DatabaseProcessing;
import org.ezcampus.search.data.StringHelper;
import org.tinylog.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("")
public class EndpointCommon
{
	private final ObjectMapper JSON_MAPPER = new ObjectMapper();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response heartBeat() 
	{
		HashMap<String, String> hm = new HashMap<String, String>();
		hm.put("Hello", "From EZCampus");
		hm.put("Our server", "Is Online");
		
		try
		{
			return Response.status(Response.Status.OK).entity(JSON_MAPPER.writeValueAsString(hm)).build();
		}
		catch (JsonProcessingException e)
		{
			Logger.error(e);
		}
		
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
	}
}
