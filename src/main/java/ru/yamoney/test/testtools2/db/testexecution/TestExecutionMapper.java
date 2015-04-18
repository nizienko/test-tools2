package ru.yamoney.test.testtools2.db.testexecution;

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
        te.setData(resultSet.getString("data"));
        te.setExecutionDt(resultSet.getTimestamp("execution_dt"));
        te.setLastChangeDt(resultSet.getTimestamp("last_change_dt"));
        return te;
    }
}
