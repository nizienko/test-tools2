package ru.yamoney.test.testtools2.db;

import org.springframework.jdbc.core.RowMapper;
import ru.yamoney.test.testtools2.testmanager.TestExecution;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by def on 02.04.15.
 */
public class TestExecutionMapper implements RowMapper<TestExecution> {
    @Override
    public TestExecution mapRow(ResultSet resultSet, int i) throws SQLException {
        TestExecution te = new TestExecution();
        te.setId(resultSet.getInt("id"));
        te.setProject(resultSet.getString("project"));
        te.setVersion(resultSet.getString("version"));
        te.setBuild(resultSet.getString("build"));
        te.setExecution(resultSet.getString("execution"));
        te.setStatus(resultSet.getInt("status"));
        te.setComment(resultSet.getString("comment"));
        te.setIssue(resultSet.getString("issue"));
        te.setName(resultSet.getString("name"));
        te.setPublicated(resultSet.getInt("publicated_status"));
        return te;
    }
}
