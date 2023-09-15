package org.ezcampus.search.api;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;

import org.ezcampus.search.core.models.request.ReportPostQuery;
import org.ezcampus.search.core.models.request.SearchQuery;
import org.ezcampus.search.core.models.response.CourseDataResult;
import org.ezcampus.search.core.models.response.GenericMessageResult;
import org.ezcampus.search.hibernate.util.HibernateUtil;
import org.ezcampus.search.hibernate.entity.Browser;
import org.ezcampus.search.hibernate.entity.OperatingSystem;
import org.ezcampus.search.hibernate.entity.Report;
import org.ezcampus.search.hibernate.entity.ReportType;
import org.ezcampus.search.hibernate.entity.WordMap;

//Hibernate Core Imports
import org.hibernate.Session;
import org.hibernate.Transaction;

import org.tinylog.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.NoResultException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


@Path("/report")
public class EndpointReport
{
    private final ObjectMapper jsonMap = new ObjectMapper();

	@POST
    @Path("/submit")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response postReport(String jsonPayload)
	{
        ReportPostQuery reportData;
        try
		{
			reportData = jsonMap.readValue(jsonPayload, ReportPostQuery.class);
		}
		catch (IOException e)
		{
			Logger.debug("Got bad json: {}", e);
            GenericMessageResult jsonError = new GenericMessageResult("Invalid JSON Payload...");

			return Response.status(Response.Status.BAD_REQUEST).entity(jsonError).build();
		}

        //* REQUIRED FIELDS FOR SUBMISSION */

        if (reportData.getReportTypeId() == 0) {
            GenericMessageResult e1 = new GenericMessageResult("Please select the type of this report.");
            return Response.status(Response.Status.BAD_REQUEST).entity(e1).build();
        }

        if (reportData.getDescription().isEmpty()) {
            GenericMessageResult e2 = new GenericMessageResult("Please include a description for this report.");
            return Response.status(Response.Status.BAD_REQUEST).entity(e2).build();
        }

        //### Write to Database ###

        Report incomingReport = new Report();
        
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
        
        try {

            String findBrowserQ = "SELECT br FROM Browser br WHERE br.browserId = :browserId";

            Browser foundBrowser = session.createQuery(findBrowserQ, Browser.class)
                .setParameter("browserId", reportData.getBrowserTypeId())
                .getSingleResult();
            incomingReport.setBrowser(foundBrowser);

            String findOsQ = "SELECT os FROM OperatingSystem os WHERE os.osId = :osId";

            OperatingSystem foundOs = session.createQuery(findOsQ, OperatingSystem.class)
                .setParameter("osId", reportData.getOsId())
                .getSingleResult();
            incomingReport.setOperatingSystem(foundOs);

            String findReportTypeQ = "SELECT rt FROM ReportType rt WHERE rt.reportTypeId = :rtId";
            
            ReportType foundReportType = session.createQuery(findReportTypeQ, ReportType.class)
                .setParameter("rtId", reportData.getReportTypeId())
                .getSingleResult();
            incomingReport.setReportType(foundReportType);

        } catch (NoResultException e) {
        
        // Return the error response
        GenericMessageResult error = new GenericMessageResult("Please make sure to fill in all dropdown menu options... (Type of Browser, Report & Operating System)");
        return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }

            //*SQL Text Construct maxium length: 65,535 BYTES
            //Assuming UTF-32, king of a worst case scenario (4 bytes per char)
            // We'll cap the Char limit to 16,300 (~65.5k / 4)

            final int MAX_CHAR_LENGTH = 16300;
            if (reportData.getDescription().length() > MAX_CHAR_LENGTH) {
                    NumberFormat numberFormat = NumberFormat.getInstance(); // This gets the default locale. If you want a specific locale, use NumberFormat.getInstance(Locale.US) for instance.
                    String formattedMaxCharLength = numberFormat.format(MAX_CHAR_LENGTH);
                    GenericMessageResult error = new GenericMessageResult(String.format("Description is longer than %s characters.", formattedMaxCharLength));
                return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
            }
            incomingReport.setDescription(reportData.getDescription());
            
            //Transaction isn't really needed here, but good practice for Atomicity

            Transaction transaction = session.beginTransaction();
            session.persist(incomingReport);
            transaction.commit();

            GenericMessageResult success = new GenericMessageResult("Thanks, we've received your report!");
            return Response.status(Response.Status.OK).entity(success).build();

        }

    }
    
    @GET
    @Path("/getall/{type}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTypes(@PathParam("type") String type) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            String queryString = null;
            Class<?> entityClass = null;
    
            switch(type) {
                case "Browser":
                    queryString = "FROM Browser";
                    entityClass = Browser.class;
                    break;
                case "OperatingSystem":
                    queryString = "FROM OperatingSystem";
                    entityClass = OperatingSystem.class;
                    break;
                case "ReportType":
                    queryString = "FROM ReportType";
                    entityClass = ReportType.class;
                    break;
                default:
                    GenericMessageResult typeError = new GenericMessageResult("Sorry, we don't support this type...");
                    return Response.status(Response.Status.BAD_REQUEST).entity(typeError).build();
            }
    
            List<?> results = session.createQuery(queryString, entityClass).getResultList();
            String jsonArray = jsonMap.writeValueAsString(results);
            // Now you can process and return the results.
            return Response.status(Response.Status.OK).entity(jsonArray).build();
    
        } catch(Exception e) {
            // Handle exceptions
             GenericMessageResult generalError = new GenericMessageResult("An error occurred... "+e.toString());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(generalError).build();
        }
    }

    //Endof Report Endpoints File
}
