package ru.yamoney.test.testtools2.teststand.resources;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yamoney.test.testtools2.common.Application;
import ru.yamoney.test.testtools2.teststand.TestStand;

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
    private String data;
    private TestStand testStand;

    @Override
    public String toString() {
        return resourceStatus.getName();
    }

    @Override
    public void init(String data) {
        try {
            this.data = data;
            dataJSON = new JSONObject(data);
            testStand = (TestStand) Application.getCtx().getBean("testStand");
            jdbcTemplate = testStand.getJdbcTemplateContainer().getJdbcTemplates().get(dataJSON.get("dataSource"));
            if (jdbcTemplate == null) {
                throw new IllegalStateException("No such dataSource");
            }
        } catch (Exception e) {
            resourceStatus.setBroken(true);
            LOG.error("Error!!: " + e.getMessage());
        }
        resourceStatus = new ResourceStatus(120);
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
        } catch (JSONException e) {
            resourceStatus.setBroken(true);
            LOG.error(e.getMessage());
        }
        try {
            expectedResult = (String) dataJSON.get("expectedResult");
        } catch (JSONException e) {
            resourceStatus.setBroken(true);
            LOG.error(e.getMessage());
        }
    }

    @Override
    public ResourceStatus getStatus() {
        return resourceStatus;
    }

    @Override
    public void checkStatus() {
        if (resourceStatus.isDataToOld()) {
            check(true);
        }
    }

    private synchronized void check(boolean first) {
        resourceStatus.updateLastCheck();
        try {
            if (SQL.toUpperCase().startsWith("SELECT") && !(SQL.replaceAll(";", "").toUpperCase().endsWith("FOR UPDATE"))) {
                String result = jdbcTemplate.queryForObject(SQL, String.class);
                if (expectedResult.equals(result)) {
                    resourceStatus.setStatus(true, "Result: " + result);
                } else {
                    resourceStatus.setStatus(false, "Result: " + result + ", expected: " + expectedResult);
                }
            } else {
                resourceStatus.setStatus(false, "bad select " + SQL);
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
            if ((first) && (e.getMessage().contains("data to read from socket"))) {
                LOG.info("Going to try second time...");
                testStand.loadDataSources();
                this.init(data);
                check(false);
            } else {
                resourceStatus.setStatus(false, e.getMessage());
            }
        }
    }
}
