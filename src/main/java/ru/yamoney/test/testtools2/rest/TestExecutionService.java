package ru.yamoney.test.testtools2.rest;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.simple.JSONObject;
import ru.yamoney.test.testtools2.common.Application;
import ru.yamoney.test.testtools2.testmanager.ReasonStatus;
import ru.yamoney.test.testtools2.testmanager.TestExecution;
import ru.yamoney.test.testtools2.testmanager.TestManager;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;

/**
 * Created by def on 02.04.15.
 */

@Path("/testExecution")
public class TestExecutionService {
    public static final Logger LOG = Logger.getLogger(TestExecutionService.class);
    private final static String BUILD = "Ad hoc";
    private final static String EXECUTION = "default";

    public void init() {

    }

    @POST
    @Path("/add")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addTestExecution(
            @FormParam("project") String project,
            @FormParam("version") String version,
            @FormParam("build") String build,
            @FormParam("execution") String execution,
            @FormParam("issue") String issue,
            @FormParam("name") String name,
            @FormParam("status") String status,
            @FormParam("comment") String comment,
            @FormParam("acceptance") String acceptance,
            @FormParam("postponedChecks") String postponedChecks
    ) {
        if ((project == null) || (project.equals(""))) {
            return Response.status(400).entity(buildJSONAnswer(-1, "project is empty")).build();
        }
        if ((version == null) || (version.equals(""))) {
            return Response.status(400).entity(buildJSONAnswer(-1, "version is empty")).build();
        }
        if ((build == null) || (build.equals(""))) {
            build = BUILD;
        }
        if ((execution == null) || (execution.equals(""))) {
            execution = EXECUTION;
        }
        if ((issue == null) || (issue.equals(""))) {
            return Response.status(400).entity(buildJSONAnswer(-1, "issue is empty")).build();
        }
        if ((name == null) || (name.equals(""))) {
            name = issue;
        }
        if ((status == null) || (status.equals(""))) {
            return Response.status(400).entity(buildJSONAnswer(-1, "status is empty")).build();
        }

        if ((comment == null)) {
            comment = "";
        }

        int executionStatus;
        try {
            executionStatus = Integer.parseInt(status);
        } catch (NumberFormatException e) {
            return Response.status(400).entity(buildJSONAnswer(-1, "bad status")).build();
        }
        TestExecution testExecution = new TestExecution();
        testExecution.setProject(project);
        testExecution.setVersion(version);
        testExecution.setBuild(build);
        testExecution.setExecution(execution);
        testExecution.setIssue(issue);
        testExecution.setName(name);
        testExecution.setStatus(executionStatus);
        testExecution.setComment(comment);
        testExecution.setExecutionDt(new Date());
        testExecution.setPublished(0);
        testExecution.setFailReason(ReasonStatus.NOT_SET.name());
        if (acceptance != null) {
            try {
                int acceptanceValue = Integer.parseInt(acceptance);
                testExecution.setAcceptance(acceptanceValue);
            } catch (NumberFormatException e) {
                LOG.error("Acceptance value not correct int: " + acceptance);
            }
        }
        if (postponedChecks != null) {
            try {
                LOG.info(postponedChecks);
                testExecution.setPostponedCheckList(new JSONArray(postponedChecks));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            TestManager testManager = (TestManager) Application.getCtx().getBean("testManager");
            testManager.addTestExecution(testExecution);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return Response.status(500).entity(buildJSONAnswer(-2, "Server error")).build();
        }
        return Response.status(200).entity(buildJSONAnswer(0, "success")).build();
    }

    private String buildJSONAnswer(int status, String message) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", status);
        if (message != null) {
            jsonObject.put("message", message);
        }
        return jsonObject.toString();
    }
}
