package org.ezcampus.search.api;

import java.io.IOException;
import java.util.List;

import org.ezcampus.search.core.models.request.ReportPostQuery;
import org.ezcampus.search.core.models.request.SearchQuery;
import org.ezcampus.search.core.models.response.CourseDataResult;
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

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


@Path("/report")
public class EndpointReport
{
    private final ObjectMapper jsonMap = new ObjectMapper();

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)

	public Response getRequestBody(String jsonPayload)
	{
        ReportPostQuery reportData;
        try
		{
			reportData = jsonMap.readValue(jsonPayload, ReportPostQuery.class);
		}
		catch (IOException e)
		{
			Logger.debug("Got bad json: {}", e);
			return Response.status(Response.Status.BAD_REQUEST).entity("Invalid JSON payload").build();
		}

        //  Now that we've read in the Data, let's make sure we've got no missing fields:
        //* Atleast Report Type & Report Description should be given

        if( reportData.getReportTypeId() == 0 || !reportData.getDescription().isEmpty() ) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Not enough info...").build();
            //TODO: Robustify error msgs
        }


        
        //### Write to Database ###

        Report incomingReport = new Report();
        
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            
            String findBrowserQ = "SELECT br FROM Browser br WHERE br.browserId = :browserId";

            Browser foundBrowser = session.createQuery(findBrowserQ, Browser.class)
                .setParameter("browserId", reportData.getBrowserTypeId())
                .getSingleResult();
            incomingReport.setBrowser(foundBrowser);

            String findOsQ = "SELECT os FROM OperatingSystem os WHERE br.osId = :osId";

            OperatingSystem foundOs = session.createQuery(findOsQ, OperatingSystem.class)
                .setParameter("osId", reportData.getOsId())
                .getSingleResult();
            incomingReport.setOperatingSystem(foundOs);

            String findReportTypeQ = "SELECT rt FROM ReportType rt WHERE rt.reportTypeId = :rtId";
            
            ReportType foundReportType = session.createQuery(findReportTypeQ, ReportType.class)
                .setParameter("rtId", reportData.getReportTypeId())
                .getSingleResult();
            incomingReport.setReportType(foundReportType);

            incomingReport.setDescription(reportData.getDescription());
            
            //Transaction isn't really needed here, but good practice for Atomicity

            Transaction transaction = session.beginTransaction();
            session.persist(incomingReport);
            transaction.commit();
            return Response.status(Response.Status.OK).entity("Successfully Submitted Your Report!").build();
        }

    }
}
