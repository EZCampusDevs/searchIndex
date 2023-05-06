package org.ezcampus.search.rest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.ezcampus.search.data.db.Database;
import org.ezcampus.search.hibernate.entity.Word;
import org.ezcampus.search.hibernate.util.SessionUtil;
import org.hibernate.Session;
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

					List<String> existingWord = new LinkedList<>();
					
					while(rs.next()) 
					{
						existingWord.add(rs.getString(2));
					}
					
					existingWord.forEach(x -> {
						Logger.info(x);
					});
					
					String a = String.join(", ", existingWord);
					
					return Response.status(Response.Status.OK).entity(a).build();
					
				}
			}
		}
		catch (SQLException e)
		{
			Logger.error(e);
		}

		return Response.status(Response.Status.NOT_FOUND).entity("not found").build();

	}

	
	@GET
	@Path("2")
	@Produces(MediaType.TEXT_PLAIN)
	public Response getRequestBody2(
			@QueryParam("search_term") String searchTerm, @QueryParam("page") int page,
			@QueryParam("results_per_page") int resultsPerPage
	)
	{

		Logger.debug("search function starting");

		
		try (Session session = SessionUtil.getSessionFactory().openSession()) 
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
}
