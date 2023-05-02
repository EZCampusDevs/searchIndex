package org.ezcampus.searchIndex;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

@Path("/api")
public class HelloResource
{
    @GET
    @Produces("text/plain")
    public String hello()
    {
        return "Hello, World!";
    }
}