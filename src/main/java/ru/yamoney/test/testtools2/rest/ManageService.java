package ru.yamoney.test.testtools2.rest;

import org.json.simple.JSONObject;
import ru.yamoney.test.testtools2.common.Application;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by def on 05.04.15.
 */
@Path("/manage")
public class ManageService {

    @GET
    @Path("/status")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStatus() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", "ok");
        return Response.status(200).entity(jsonObject.toString()).build();
    }

    @GET
    @Path("/stop")
    @Produces(MediaType.APPLICATION_JSON)
    public Response stop() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", "stopping...");
        Application.stopAllThreads();
        return Response.status(200).entity(jsonObject.toString()).build();
    }
}
