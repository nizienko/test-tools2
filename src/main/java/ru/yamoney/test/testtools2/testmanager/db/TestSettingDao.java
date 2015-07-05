package ru.yamoney.test.testtools2.testmanager.db;


import org.springframework.jdbc.core.JdbcTemplate;
import ru.yamoney.test.testtools2.testmanager.TestConfiguration;
import ru.yamoney.test.testtools2.testmanager.TestParameter;
import ru.yamoney.test.testtools2.testmanager.TestSetting;

import java.util.List;


/**
 * Created by def on 12.11.14.
 */
public class TestSettingDao {
    private JdbcTemplate jdbcTemplate;

    public TestSettingDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public void insertTestConfiguration(String name, String description) {
        String SQL = "insert into testconfiguration (tk, description) values (?, ?);";
        jdbcTemplate.update(SQL, name, description);
    }

    public void insertParameter(String name, String description) {
        String SQL = "insert into parameters (parameter, description) values (?, ?);";
        jdbcTemplate.update(SQL, name, description);
    }

    public void updateTestConfiguration(Integer id, String name, String description) {
        String SQL = "update testconfiguration set tk=?, description=? where id=?";
        jdbcTemplate.update(SQL,
                name,
                description,
                id
        );
    }

    public void updateParameter(TestParameter testParameter) {
        String SQL = "update parameters set parameter=?, description=? where id=?";
        jdbcTemplate.update(SQL,
                testParameter.getName(),
                testParameter.getDescription(),
                testParameter.getId()
        );
    }

    public void insertValue(TestConfiguration tk, TestParameter parameter, String value) {
        String SQL = "insert into testvalues (tk_id, parameter_id, value) values (?, ?, ?);";
        jdbcTemplate.update(SQL, tk.getId(), parameter.getId(), value);
    }

    public void updateValue(TestConfiguration tk, TestSetting testSetting) {
        String SQL = "update testvalues set value=? where " +
                "tk_id=? " +
                "and parameter_id in (select id from parameters where parameter=?)";
        jdbcTemplate.update(SQL,
                testSetting.getValue(),
                tk.getId(),
                testSetting.getParameterName()
        );
    }

    public List<TestSetting> selectByTestConfiguration(TestConfiguration tk) {
        String SQL = "select p.parameter, v.value, p.description from parameters p left join ( \n" +
                "select tv.parameter_id, tv.value from testvalues tv where tk_id=?\n" +
                ") v\n" +
                "on p.id=v.parameter_id";
        return jdbcTemplate.query(SQL, new Object[]{tk.getId()}, new TestSettingMapper());
    }

    // like ? Почему-то не работает like '%?%'
    public List<TestSetting> selectByTestConfigurationContains(TestConfiguration tk, String param) {
        String SQL = "select p.parameter as parameter, v.value as value, p.description description from parameters p left join ( \n" +
                "select tv.parameter_id, tv.value from testvalues tv where tk_id=?\n" +
                ") v\n" +
                "on p.id=v.parameter_id where p.parameter like '%" + param + "%'";
        return jdbcTemplate.query(SQL, new Object[]{tk.getId()}, new TestSettingMapper());
    }

    public List<TestConfiguration> selectTestConfigurations() {
        String SQL = "select id, tk, description from testconfiguration;";
        return jdbcTemplate.query(SQL, new Object[]{}, new TestConfigurationMapper());
    }

    public List<TestParameter> selectAllParameters() {
        String SQL = "select id, parameter, description from parameters;";
        return jdbcTemplate.query(SQL, new Object[]{}, new TestParameterMapper());
    }

    public TestConfiguration selectTestConfigurationByName(String tk) {
        String SQL = "select id, tk, description from testconfiguration where tk=?;";
        return jdbcTemplate.queryForObject(SQL, new Object[]{tk}, new TestConfigurationMapper());
    }

    public TestSetting selectTestSetting(TestConfiguration tk, String param) {
        String SQL = "select p.parameter AS parameter, tv.value AS value, p.description AS description " +
                "from testvalues tv join parameters p " +
                "on tv.parameter_id=p.id " +
                "where tv.tk_id=? and p.parameter=?;";
        return jdbcTemplate.queryForObject(SQL, new Object[]{tk.getId(), param}, new TestSettingMapper());
    }

    public TestParameter selectParameter(String param) {
        String SQL = "select id, parameter, description from parameters where parameter=?";
        return jdbcTemplate.queryForObject(SQL, new Object[]{param}, new TestParameterMapper());
    }

    public void deleteParameter(TestParameter testParameter) {
        String SQL = "delete from parameters where id=?";
        jdbcTemplate.update(SQL, testParameter.getId());

        SQL = "delete from testvalues where parameter_id=?";
        jdbcTemplate.update(SQL, testParameter.getId());
    }

}
