package ru.yamoney.test.testtools2.testmanager.db;


import org.springframework.jdbc.core.RowMapper;
import ru.yamoney.test.testtools2.testmanager.TestConfiguration;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by nizienko on 12.11.14.
 */
public class TestConfigurationMapper implements RowMapper<TestConfiguration> {
    public TestConfiguration mapRow(ResultSet resultSet, int i) throws SQLException {
        TestConfiguration testConfiguration = new TestConfiguration();
        testConfiguration.setId(resultSet.getInt("id"));
        testConfiguration.setName(resultSet.getString("tk"));
        testConfiguration.setDescription("description");
        return testConfiguration;
    }
}
