package ru.yamoney.test.testtools2.db.testexecution;

import org.springframework.jdbc.core.JdbcTemplate;
import ru.yamoney.test.testtools2.testmanager.ExecutionStatus;
import ru.yamoney.test.testtools2.testmanager.TestExecution;
import ru.yamoney.test.testtools2.vaadin.testresults.TestResultsFilter;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by def on 02.04.15.
 */
public class TestExecutionDao {
    private JdbcTemplate jdbcTemplate;

    public TestExecutionDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insert(TestExecution te){
        String SQL = "INSERT INTO execution(\n" +
                "            data, execution_dt, last_change_dt)\n" +
                "    VALUES (?::jsonb, ?, ?);";
        jdbcTemplate.update(SQL, new Object[]{
                te.getData(), te.getExecutionDt(), new Date()
        });
    }

    public List<TestExecution> getAll() {
        String SQL = "SELECT id, data, execution_dt, last_change_dt\n" +
                "  FROM execution;\n";
        return jdbcTemplate.query(SQL, new TestExecutionMapper());
    }

    public List<TestExecution> getNotPublished(){
        String SQL = "SELECT id, data, execution_dt, last_change_dt\n" +
                "  FROM execution where data->>'publicated' = '0';\n";
        return jdbcTemplate.query(SQL, new TestExecutionMapper());
    }

    public void updateTestExecution(TestExecution te) {
        String SQL = "UPDATE execution\n" +
                "   SET data=?::jsonb, execution_dt=?, last_change_dt=? \n" +
                " WHERE id=?";

        jdbcTemplate.update(SQL, new Object[]{
                te.getData(), te.getExecutionDt(), new Date(), te.getId()
        });
    }

    public List<TestExecution> getByFilter(TestResultsFilter filter) {
        StringBuffer sqlText = new StringBuffer();
        sqlText.append("SELECT id, data, execution_dt, last_change_dt FROM execution");
        sqlText.append(filter.getSql());
        sqlText.append(" order by execution_dt desc limit 10000;");
        String SQL = sqlText.toString();
        return jdbcTemplate.query(SQL, filter.getObjects(), new TestExecutionMapper());
    }

    public Integer countByFilter(TestResultsFilter filter) {
        StringBuffer sqlText = new StringBuffer();
        sqlText.append("SELECT count(*) FROM execution");
        sqlText.append(filter.getSql());
        sqlText.append(";");
        String SQL = sqlText.toString();
        System.out.println(SQL);
        return jdbcTemplate.queryForObject(SQL, filter.getObjects(), Integer.class);
    }

    public TestExecution getLastPassed(String issue) {
        String SQL = "SELECT id, data, execution_dt, last_change_dt FROM execution " +
                "where data->>'status'=? and data->>'issue'=? " +
                "order by execution_dt desc limit 1;";
        return jdbcTemplate.queryForObject(SQL, new Object[]{
                ExecutionStatus.PASSED.getValue(), issue}, new TestExecutionMapper());
    }


    public List<java.util.Map<String, Object>> selectProjects(){
        String SQL = "SELECT distinct data->>'project' AS project FROM execution;";

        return jdbcTemplate.queryForList(SQL);
    }

    public List<Map<String, Object>> selectVersions(TestResultsFilter filter) {
        String SQL = "SELECT distinct data->>'version' AS version FROM execution where " +
                "data->>'project'=?";
        return jdbcTemplate.queryForList(SQL, filter.getProject());
    }

    public List<Map<String, Object>> selectBuilds(TestResultsFilter filter) {
        String SQL = "SELECT distinct data->>'build' AS build FROM execution where " +
                "data->>'project'=? and data->>'version'=?";
        return jdbcTemplate.queryForList(SQL, filter.getProject(), filter.getVersion());
    }

    public List<Map<String, Object>> selectExecutions(TestResultsFilter filter) {
        String SQL = "SELECT distinct data->>'execution' AS execution FROM execution where " +
                "data->>'project'=? and data->>'version'=? and data->>'build'=?";
        return jdbcTemplate.queryForList(SQL, filter.getProject(), filter.getVersion(), filter.getBuild());
    }

    public List<Map<String, Object>> selectIssues(TestResultsFilter filter) {
        StringBuffer sqlText = new StringBuffer();
        sqlText.append("SELECT distinct data->>'issue' AS issue FROM execution");
        sqlText.append(filter.getSql());
        sqlText.append(" order by issue;");
        String SQL = sqlText.toString();
        return jdbcTemplate.queryForList(SQL, filter.getObjects());
    }

    public String getNameByIssue(String issue){
        String SQL = "SELECT data->>'name' AS name from execution where data->>'issue'=? order by execution_dt desc limit 1";
        return jdbcTemplate.queryForObject(SQL, new Object[]{issue}, String.class);
    }
}
