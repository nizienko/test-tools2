package ru.yamoney.test.testtools2.testmanager.db;

import org.springframework.jdbc.core.RowMapper;
import ru.yamoney.test.testtools2.testmanager.TestSetting;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by def on 12.11.14.
 */
public class TestSettingMapper implements RowMapper<TestSetting> {
    public TestSetting mapRow(ResultSet resultSet, int i) throws SQLException {
        TestSetting testSetting = new TestSetting();
        testSetting.setParameterName(resultSet.getString("parameter"));
        testSetting.setDescription(resultSet.getString("description"));
        testSetting.setValue(resultSet.getString("value"));
        return testSetting;
    }
}
