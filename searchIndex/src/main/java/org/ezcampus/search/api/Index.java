package org.ezcampus.search.api;

import java.net.URI;

import org.ezcampus.search.core.models.response.StatusResponse;
import org.ezcampus.search.data.DatabaseProcessing;
import org.ezcampus.search.data.ProcessDatabaseJob;
import org.ezcampus.search.data.StringHelper;
import org.ezcampus.search.data.threading.ThreadCallCallback;
import org.ezcampus.search.data.threading.ThreadHandling;
import org.tinylog.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;

@Path("/index")
public class Index
{
	private final ObjectMapper JSON_MAPPER = new ObjectMapper();
	
	@GET
	@Path("/status")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getIndexStatus() 
	{
		StatusResponse s = new StatusResponse();
		s.isProcessing = DatabaseProcessing.isProcessing();
		s.startTime = DatabaseProcessing.getStartTime();
		s.timeSinceStart = DatabaseProcessing.getProcessElapsedTime();
		s.timeSinceStartPretty = StringHelper.getEllapsedTimePretty(s.timeSinceStart);
		
		try
		{
			return Response.status(Response.Status.OK).entity(JSON_MAPPER.writeValueAsString(s)).build();
		}
		catch (JsonProcessingException e)
		{
			Logger.error(e);
		}
		
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
	}
	
	
	@GET
	@Path("/trigger")
	@Produces(MediaType.APPLICATION_JSON)
	public Response triggerDatabaseIndex() 
	{
		if(DatabaseProcessing.isProcessing())
			return Response.status(Response.Status.CONFLICT).entity("Job already running").build();

		ThreadCallCallback processDatabaseJob = new ProcessDatabaseJob();
		
		ThreadHandling.callToThread(processDatabaseJob);
		
        URI uri = UriBuilder.fromPath("/index/status").build();

		return Response.seeOther(uri).build();
	}
	
}
