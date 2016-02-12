package ru.yamoney.test.testtools2.rest;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import ru.yamoney.test.testtools2.common.Application;
import ru.yamoney.test.testtools2.teststand.TestStand;
import ru.yamoney.test.testtools2.teststand.resources.Resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by nizienko on 10.02.2016.
 */

@Path("/test-stand")
public class TestStandStatusService {
    public static final Logger LOG = Logger.getLogger(TestStandStatusService.class);

    @GET
    @Path("/status")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSettings(){
        TestStand testStand = (TestStand) Application.getCtx().getBean("testStand");
        JSONObject jsonObject = new JSONObject();
        testStand.getResources().stream().forEach((Resource r) -> jsonObject.put(r.toString(),
                r.getStatus().getStatusMeassage()));
        return Response.status(200).entity(jsonObject.toString()).build();
    }

}
