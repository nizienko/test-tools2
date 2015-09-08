package ru.yamoney.test.testtools2.testmanager.db;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import ru.yamoney.test.testtools2.postponecheck.PostponedCheck;
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

    public void insert(TestExecution te) {
        String SQL = "INSERT INTO execution(\n" +
                "            data, execution_dt, last_change_dt)\n" +
                "    VALUES (?::jsonb, ?, ?);";
        jdbcTemplate.update(SQL, new Object[]{
                te.getData(), te.getExecutionDt(), new Date()
        });
    }

    public TestExecution get(int id) {
        String SQL = "SELECT id, data, execution_dt, last_change_dt\n" +
                "  FROM execution where id=?;\n";
        return jdbcTemplate.queryForObject(SQL, new Object[]{id},
                new TestExecutionMapper());
    }

    public List<TestExecution> getAll() {
        String SQL = "SELECT id, data, execution_dt, last_change_dt\n" +
                "  FROM execution;\n";
        return jdbcTemplate.query(SQL, new TestExecutionMapper());
    }

    public List<TestExecution> getNotPublished() {
        String SQL = "SELECT id, data, execution_dt, last_change_dt\n" +
                "  FROM execution where data->>'publicated' = '0';\n";
        return jdbcTemplate.query(SQL, new TestExecutionMapper());
    }

    public List<TestExecution> getProcessing() {
        String SQL = "SELECT id, data, execution_dt, last_change_dt\n" +
                "  FROM execution where data->>'status'=?;\n";
        return jdbcTemplate.query(SQL, new Object[]{
                ExecutionStatus.PROCESSING.getValue()}, new TestExecutionMapper());
    }


    @Transactional
    public void setPublished(TestExecution te) {
        TestExecution te2 = get(te.getId());
        te2.setPublished(1);
        updateTestExecution(te2);
    }

    @Transactional
    public void setFailedReason(int id, String status) {
        TestExecution te = get(id);
        te.setFailReason(status);
        updateTestExecution(te);
    }

    @Transactional
    public void setComment(int id, String comment) {
        TestExecution te = get(id);
        te.setComment(comment);
        updateTestExecution(te);
    }


    @Transactional
    public synchronized void setReasonComment(int id, String comment) {
        TestExecution te = get(id);
        te.setReasonComment(comment);
        updateTestExecution(te);
    }

    @Transactional
    public synchronized void setStatus(int id, ExecutionStatus status) {
        TestExecution te = get(id);
        te.setStatus(status.getIntegerValue());
        updateTestExecution(te);
    }

    @Transactional
    public synchronized void updatePostponedChecks(int id, List<PostponedCheck> checks) {
        TestExecution te = get(id);
        te.setPostponedCheckList(checks);
        updateTestExecution(te);
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
        System.out.println(SQL);
        return jdbcTemplate.query(SQL, filter.getObjects(), new TestExecutionMapper());
    }

    public Integer countByFilter(TestResultsFilter filter) {
        String SQL = "SELECT count(*) FROM execution" + filter.getSql() + ";";
        return jdbcTemplate.queryForObject(SQL, filter.getObjects(), Integer.class);
    }

    public TestExecution getLastPassed(String issue) {
        String SQL = "SELECT id, data, execution_dt, last_change_dt FROM execution " +
                "where data->>'status'=? and data->>'issue'=? " +
                "order by execution_dt desc limit 1;";
        return jdbcTemplate.queryForObject(SQL, new Object[]{
                ExecutionStatus.PASSED.getValue(), issue}, new TestExecutionMapper());
    }

    public List<java.util.Map<String, Object>> selectProjects() {
        return selectProjects(null);
    }

    public List<java.util.Map<String, Object>> selectProjects(TestResultsFilter filter) {
        if (filter == null) {
            String SQL = "SELECT distinct data->>'project' AS project FROM execution;";
            return jdbcTemplate.queryForList(SQL);
        } else {
            String SQL = "SELECT distinct data->>'project' AS project FROM execution" + filter.getSql() + " order by project;";
            return jdbcTemplate.queryForList(SQL, filter.getObjects());
        }
    }

    public List<Map<String, Object>> selectVersions(TestResultsFilter filter) {
        String SQL = "SELECT distinct data->>'version' AS version FROM execution" + filter.getSql() + " order by version desc;";
        return jdbcTemplate.queryForList(SQL, filter.getObjects());
    }

    public List<Map<String, Object>> selectBuilds(TestResultsFilter filter) {
        String SQL = "SELECT distinct data->>'build' AS build FROM execution" + filter.getSql() + " order by build desc;";
        return jdbcTemplate.queryForList(SQL, filter.getObjects());
    }

    public List<Map<String, Object>> selectExecutions(TestResultsFilter filter) {
        String SQL = "SELECT distinct data->>'execution' AS execution FROM execution" + filter.getSql() + " order by execution desc;";
        return jdbcTemplate.queryForList(SQL, filter.getObjects());
    }

    public List<Map<String, Object>> selectIssues(TestResultsFilter filter) {
        String SQL = "SELECT distinct data->>'issue' AS issue FROM execution" + filter.getSql() + " order by issue;";
        return jdbcTemplate.queryForList(SQL, filter.getObjects());
    }

    public String getNameByIssue(String issue) {
        String SQL = "SELECT data->>'name' AS name from execution where data->>'issue'=? order by execution_dt desc limit 1";
        return jdbcTemplate.queryForObject(SQL, new Object[]{issue}, String.class);
    }
}
