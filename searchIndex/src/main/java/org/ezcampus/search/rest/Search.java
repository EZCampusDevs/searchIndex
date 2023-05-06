package org.ezcampus.search.rest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.ezcampus.search.data.db.Database;
import org.tinylog.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/search")
public class Search
{

	private final ObjectMapper jsonMap = new ObjectMapper();

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public Response getRequestBody(
			@QueryParam("search_term") String searchTerm, @QueryParam("page") int page,
			@QueryParam("results_per_page") int resultsPerPage
	)
	{

		Logger.debug("search function starting");

		try
		{

			try (Connection c = Database.getConnection())
			{
				final String SQL = "SELECT * FROM tbl_word";

				try (PreparedStatement pstmt = c.prepareStatement(SQL))
				{
					ResultSet rs = pstmt.executeQuery();

					if (rs.next())
					{
						return Response.status(Response.Status.OK).entity(rs.getString(1)).build();
					}
				}
			}
		}
		catch (SQLException e)
		{
			Logger.error(e);
		}

		return Response.status(Response.Status.NOT_FOUND).entity("not found").build();

	}

}
