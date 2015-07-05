package ru.yamoney.test.testtools2.testmanager.db;


import org.springframework.jdbc.core.RowMapper;
import ru.yamoney.test.testtools2.testmanager.TestParameter;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by def on 13.11.14.
 */
public class TestParameterMapper implements RowMapper<TestParameter> {
    public TestParameter mapRow(ResultSet resultSet, int i) throws SQLException {
        TestParameter testParameter = new TestParameter();
        testParameter.setId(resultSet.getInt("id"));
        testParameter.setName(resultSet.getString("parameter"));
        testParameter.setDescription(resultSet.getString("description"));
        return testParameter;
    }
}
