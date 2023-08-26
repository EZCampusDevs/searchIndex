package org.ezcampus.search.api;

import java.util.HashMap;

import org.ezcampus.search.hibernate.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.tinylog.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("status")
public class EndpointStatus
{
	private final ObjectMapper JSON_MAPPER = new ObjectMapper();

	public Response responseJson(HashMap<String, String> hm)
	{
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

	@GET
	@Path("database")
	@Produces(MediaType.APPLICATION_JSON)
	public Response database()
	{
		HashMap<String, String> hm = new HashMap<String, String>();
		
		try
		{
			try (Session session = HibernateUtil.getSessionFactory().openSession())
			{
				session.createNativeQuery("SELECT DATABASE() AS dbname", String.class)
					   .uniqueResult();
				
				hm.put("database_online", "true");
			}
		}
		catch (Exception e)
		{
			Logger.error(e);
			hm.put("database_online", "false");
		}


		return responseJson(hm);
	}
}
