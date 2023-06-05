package org.ezcampus.search.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import java.util.List;

import org.ezcampus.search.hibernate.util.HibernateUtil;
import org.hibernate.Session;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

//Models
import org.ezcampus.search.core.models.request.TermsQuery;

//Hibernate Entities:
import org.ezcampus.search.hibernate.entity.Term;
import org.ezcampus.search.hibernate.entity.School;


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
public class SchoolPath
{
    private final ObjectMapper jsonMap = new ObjectMapper();
	
    @Path("term")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
    public Response getRequestBody(String jsonPayload) {
        try {

			TermsQuery requestData = jsonMap.readValue(jsonPayload, TermsQuery.class);
            
            try (Session session = HibernateUtil.getSessionFactory().openSession() )
            {
                        
            String queryString = "FROM School s WHERE s.schoolUniqueValue = :sN";

            School existingSchool = session.createQuery(queryString, School.class)
                    .setParameter("sN", requestData.getSchoolName())
                    .getSingleResult();

            //Get all terms offered from that School
            String queryString1 = "SELECT t FROM Term t WHERE t.school = :existingSchool";

            List<Term> terms = session.createQuery(queryString1, Term.class)
                    .setParameter("existingSchool", existingSchool)
                    .getResultList();

                // Convert to JSON array string
			try {
				String jsonArray = jsonMap.writeValueAsString(terms);

				return Response.status(Response.Status.OK).entity(jsonArray).build(); // Response with 200 OK and data
																						// (search results)
			} catch (JsonProcessingException e) { //Error handling for JSON building of results
				e.printStackTrace();
			}


            return Response.status(Response.Status.NOT_FOUND).entity("404 not found...").build();
            
            }


        } catch (IOException e) {
                return Response.status(Response.Status.BAD_REQUEST).entity("Invalid JSON payload").build();
        }

    }
}