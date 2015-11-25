package ru.yamoney.test.testtools2.rest;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.dao.EmptyResultDataAccessException;
import ru.yamoney.test.testtools2.common.Application;
import ru.yamoney.test.testtools2.user_cashe.User;
import ru.yamoney.test.testtools2.user_cashe.UserManager;
import ru.yamoney.test.testtools2.user_cashe.UserStatus;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by def on 22.11.15.
 */

@Path("/user")
public class CashedUserService {
    public static final Logger LOG = Logger.getLogger(CashedUserService.class);

    @GET
    @Path("/{host}/{type}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(
            @PathParam("host") String host,
            @PathParam("type") String type
            ) {
        try {
            final UserManager userManager = (UserManager) Application.getCtx().getBean("userManager");
            User user = null;
            if (UserStatus.NEW.getRestName().equals(type)) {
                user = userManager.getUser(host, UserStatus.NEW);
            } else if (UserStatus.USED.getRestName().equals(type)) {
                try {
                    user = userManager.getUser(host, UserStatus.USED);
                }
                catch (EmptyResultDataAccessException e) {
                    user = userManager.getUser(host, UserStatus.NEW);
                }
            } else if (UserStatus.BUSY.getRestName().equals(type)) {
                user = userManager.getUser(host, UserStatus.BUSY);
            }
            if (user != null) {
                return Response.status(200).entity(user.getJsonString()).build();
            }
            else {
                throw new IllegalStateException("no such user");
            }
        }
        catch (Exception e) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("error", e.getMessage());
            return Response.status(500).entity(jsonObject.toString()).build();
        }
    }

    @POST
    @Path("/add")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addUser(
            @FormParam("account") String account,
            @FormParam("userName") String userName,
            @FormParam("phone") String phone,
            @FormParam("host") String host,
            @FormParam("password") String password

    ) {
        try {
            UserManager userManager = (UserManager) Application.getCtx().getBean("userManager");
            User user = userManager.addUser(account, host, phone, userName, password);
            return Response.status(200).entity(user.getJsonString()).build();
        }
        catch (Exception e) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("error", e.getMessage());
            return Response.status(500).entity(jsonObject.toString()).build();
        }
    }

    @GET
    @Path("/del/{account}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delUser(
            @PathParam("account") String account
            ) {
        try {
            UserManager userManager = (UserManager) Application.getCtx().getBean("userManager");
            userManager.delUser(account);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("status", "deleted");
            return Response.status(200).entity(jsonObject.toString()).build();
        }
        catch (Exception e) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("error", e.getMessage());
            return Response.status(500).entity(jsonObject.toString()).build();
        }
    }

    @GET
    @Path("/free/{account}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response freeUser(
            @PathParam("account") String account
    ) {
        try {
            UserManager userManager = (UserManager) Application.getCtx().getBean("userManager");
            userManager.freeUser(account);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("status", account + " is free now");
            return Response.status(200).entity(jsonObject.toString()).build();
        }
        catch (Exception e) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("error", e.getMessage());
            return Response.status(500).entity(jsonObject.toString()).build();
        }
    }
}
