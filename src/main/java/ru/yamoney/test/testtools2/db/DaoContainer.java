package ru.yamoney.test.testtools2.db;

import org.springframework.jdbc.core.JdbcTemplate;
import ru.yamoney.test.testtools2.db.testexecution.TestExecutionDao;
import ru.yamoney.test.testtools2.db.testsettings.TestSettingDao;

/**
 * Created by def on 02.04.15.
 */
public class DaoContainer {
    private TestExecutionDao testExecutionDao;
    private TestSettingDao testSettingDao;

    public DaoContainer(JdbcTemplate jdbcTemplate) {

        testExecutionDao = new TestExecutionDao(jdbcTemplate);
        testSettingDao = new TestSettingDao(jdbcTemplate);
    }

    public TestExecutionDao getTestExecutionDao() {
        return testExecutionDao;
    }

    public TestSettingDao getTestSettingDao() {
        return testSettingDao;
    }
}
