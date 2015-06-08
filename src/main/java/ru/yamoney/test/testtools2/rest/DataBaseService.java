package ru.yamoney.test.testtools2.rest;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yamoney.test.testtools2.common.Application;
import ru.yamoney.test.testtools2.teststand.TestStand;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

/**
 * Created by def on 07.06.15.
 */
@Path("/db")
public class DataBaseService {
    public static final Logger LOG = Logger.getLogger(DataBaseService.class);

    @GET
    @Path("/{dataSource}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSettings(
            @PathParam("dataSource") String dataSource,
            @QueryParam(value = "sql")
            String sql
    ){
        LOG.info("sql=" + sql);
        TestStand testStand = (TestStand) Application.getCtx().getBean("testStand");
        JdbcTemplate jdbcTemplate = testStand.getJdbcTemplateContainer().getJdbcTemplates().get(dataSource);
        if (jdbcTemplate == null) {
            JSONObject errorJsonObject = new JSONObject();
            errorJsonObject.put("error", "No such dataSource");
            return Response.status(200).entity(errorJsonObject.toString()).build();
        }
        JSONObject jsonObject = new JSONObject();
        int i = 1;
        try {
            if (sql.toUpperCase().startsWith("SELECT")) {
                sql = sql.toUpperCase().replace("FOR UPDATE", "");
                List<Map<String, Object>> result = jdbcTemplate.queryForList(sql);
                for (Map<String, Object> str: result) {
                    JSONObject strObject = new JSONObject();
                    for (Map.Entry<String, Object> entry : str.entrySet())
                    {
                        strObject.put(entry.getKey(), entry.getValue());
                    }
                    jsonObject.put(i, strObject);
                    i++;
                }
            }
            else {
                JSONObject errorJsonObject = new JSONObject();
                errorJsonObject.put("error", "sql must starts with select");
                return Response.status(200).entity(errorJsonObject.toString()).build();
            }
        }
        catch (Exception e) {
            JSONObject errorJsonObject = new JSONObject();
            errorJsonObject.put("error", e.getMessage());
            return Response.status(200).entity(errorJsonObject.toString()).build();
        }
        return Response.status(200).entity(jsonObject.toString()).build();
    }

}
