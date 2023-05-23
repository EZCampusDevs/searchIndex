package org.ezcampus.hello.api;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/test")
public class Test
{
	@GET
	public Response helloWorld() {
		return Response.status(Response.Status.OK).entity("Hello World!").build();
	}
}
