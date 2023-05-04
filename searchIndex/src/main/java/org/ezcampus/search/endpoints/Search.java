package org.ezcampus.search.endpoints;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.json.JSONObject;

// JAVAX Json Core


@Path("/search")
public class Search {
    
        JSONObject jsonObject = new JSONObject();
    
        
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRequestBody(@QueryParam("limit") int limit) {
        jsonObject.put("message", limit);
        
        String jsonString = jsonObject.toString();
        
        return Response
                .status(200) //Status always first! Builds 200 Response
                .entity(jsonString) //Add JSON body
                .build(); 
               
    }
    
    
}
