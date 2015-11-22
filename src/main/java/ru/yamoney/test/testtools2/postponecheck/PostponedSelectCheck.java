package ru.yamoney.test.testtools2.postponecheck;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yamoney.test.testtools2.common.Application;
import ru.yamoney.test.testtools2.common.Utils;
import ru.yamoney.test.testtools2.testmanager.ExecutionStatus;
import ru.yamoney.test.testtools2.teststand.TestStand;

import java.util.Date;

/**
 * User: nizienko
 * Date: 08.09.2015
 * Time: 11:25
 */
public class PostponedSelectCheck implements PostponedCheck {
    public static final Logger LOG = Logger.getLogger(PostponedSelectCheck.class);

    private String description;
    private String select;
    private String expectedResult;
    private String dataSource;
    private Integer executionStatus;
    private Date scheduledDate;

    public PostponedSelectCheck(JSONObject jsonObject) {
        try {
            description = jsonObject.getString("description");
            select = jsonObject.getString("select");
            scheduledDate = Utils.getDateFormat().parse(jsonObject.getString("scheduledDate"));
            expectedResult = jsonObject.getString("expectedResult");
            dataSource = jsonObject.getString("dataSource");

            try {
                executionStatus = jsonObject.getInt("executionStatus");
                if (executionStatus == null) {
                    executionStatus = ExecutionStatus.PROCESSING.getIntegerValue();
                }
            } catch (JSONException e) {
                executionStatus = ExecutionStatus.PROCESSING.getIntegerValue();
            }

        } catch (Exception e) {
            executionStatus = ExecutionStatus.FAILED.getIntegerValue();
        }
    }

    @Override
    public JSONObject getJSON() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("description", description);
            jsonObject.put("select", select);
            jsonObject.put("scheduledDate", Utils.getDateFormat().format(scheduledDate));
            jsonObject.put("expectedResult", expectedResult);
            jsonObject.put("executionStatus", String.valueOf(executionStatus));
            jsonObject.put("dataSource", dataSource);
            jsonObject.put("type", String.valueOf(PostponedCheckType.SELECT.getValue()));
            return jsonObject;
        } catch (JSONException e) {
            return new JSONObject();
        }
    }

    public void process() {
        if (executionStatus == ExecutionStatus.PROCESSING.getIntegerValue()
                && new Date().compareTo(scheduledDate) > 0) {
            TestStand testStand = (TestStand) Application.getCtx().getBean("testStand");
            JdbcTemplate jdbcTemplate = testStand.getJdbcTemplateContainer().getJdbcTemplates().get(dataSource);
            if (jdbcTemplate == null) {
                executionStatus = ExecutionStatus.FAILED.getIntegerValue();
            } else {
                try {
                    if (select.toUpperCase().startsWith("SELECT") && !(select.toUpperCase().contains("FOR UPDATE"))) {
                        LOG.info("select: " + select);
                        String result = jdbcTemplate.queryForObject(select, String.class);
                        LOG.info("result: " + result);
                        if (expectedResult.equals(result)) {
                            executionStatus = ExecutionStatus.PASSED.getIntegerValue();
                        } else {
                            executionStatus = ExecutionStatus.FAILED.getIntegerValue();
                            description = description + " (" + result + ")";
                        }
                    } else {
                        executionStatus = ExecutionStatus.FAILED.getIntegerValue();
                        description = description + " (bad select)";
                    }
                } catch (Exception e) {
                    description = description + " (" + e.getMessage() + ")";
                    e.printStackTrace();
                    executionStatus = ExecutionStatus.FAILED.getIntegerValue();
                    if ((e.getMessage().contains("data to read from socket"))) {
                        LOG.info("Going to try second time...");
                        testStand.loadDataSources();
                        jdbcTemplate = testStand.getJdbcTemplateContainer().getJdbcTemplates().get(dataSource);
                        String result = jdbcTemplate.queryForObject(select, String.class);
                        if (expectedResult.equals(result)) {
                            executionStatus = ExecutionStatus.PASSED.getIntegerValue();
                        } else {
                            executionStatus = ExecutionStatus.FAILED.getIntegerValue();
                            description = description + " (" + result + ")";
                        }
                    }
                }
            }
        }
    }

    public int getStatus() {
        return executionStatus;
    }

    public String getDescription() {
        return description;
    }

}
