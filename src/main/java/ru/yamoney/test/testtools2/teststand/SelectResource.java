package ru.yamoney.test.testtools2.teststand;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yamoney.test.testtools2.common.Application;

/**
 * Created by def on 06.06.15.
 */
public class SelectResource implements Resource {
    public static final Logger LOG = Logger.getLogger(SelectResource.class);
    private ResourceStatus resourceStatus;
    private JSONObject dataJSON;
    private JdbcTemplate jdbcTemplate;
    private String SQL;
    private String expectedResult;

    @Override
    public String toString(){
        return resourceStatus.getName();
    }

    @Override
    public void init(String data) {
        try {
            dataJSON = new JSONObject(data);
            TestStand testStand = (TestStand) Application.getCtx().getBean("testStand");
            jdbcTemplate = testStand.getJdbcTemplateContainer().getJdbcTemplates().get(dataJSON.get("dataSource"));
            if (jdbcTemplate == null) {
                throw new IllegalStateException("No such dataSource");
            }
        } catch (Exception e) {
            resourceStatus.setBroken(true);
            LOG.error("Error!!: " + e.getMessage());
        }
        resourceStatus = new ResourceStatus(60);
        try {
            String name = (String) dataJSON.get("name");
            resourceStatus.setName(name);
        } catch (JSONException e) {
            LOG.error(e.getMessage());
            try {
                resourceStatus.setName((String) dataJSON.get("serverName"));
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
        try {
            SQL = (String) dataJSON.get("SQL");
        }
        catch (JSONException e) {
            resourceStatus.setBroken(true);
            LOG.error(e.getMessage());
        }
        try {
            expectedResult = (String) dataJSON.get("expectedResult");
        }
        catch (JSONException e) {
            resourceStatus.setBroken(true);
            LOG.error(e.getMessage());
        }
    }

    @Override
    public ResourceStatus getStatus() {
        if (resourceStatus.isDataToOld()) {
            check();
        }
        return resourceStatus;
    }

    private void check(){
        try {
            String result = jdbcTemplate.queryForObject(SQL, String.class);
            if (expectedResult.equals(result)) {
                resourceStatus.setStatus(true, "Result: " + result);
            }
            else {
                resourceStatus.setStatus(false, "Result: " + result + ", expected: " + expectedResult);
            }
        }
        catch (Exception e) {
            resourceStatus.setStatus(false, e.getMessage());
        }
    }
}
