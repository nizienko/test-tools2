package ru.yamoney.test.testtools2.common;

import org.springframework.jdbc.core.JdbcTemplate;
import ru.yamoney.test.testtools2.teststand.services.db.ServiceDao;
import ru.yamoney.test.testtools2.teststand.resources.db.TestStandDao;
import ru.yamoney.test.testtools2.testmanager.db.TestExecutionDao;
import ru.yamoney.test.testtools2.testmanager.db.TestSettingDao;

/**
 * Created by def on 02.04.15.
 */
public class DaoContainer {
    private TestExecutionDao testExecutionDao;
    private TestSettingDao testSettingDao;
    private TestStandDao testStandDao;
    private ServiceDao serviceDao;

    public DaoContainer(JdbcTemplate jdbcTemplate) {

        testExecutionDao = new TestExecutionDao(jdbcTemplate);
        testSettingDao = new TestSettingDao(jdbcTemplate);
        testStandDao = new TestStandDao(jdbcTemplate);
        serviceDao = new ServiceDao(jdbcTemplate);
    }

    public TestExecutionDao getTestExecutionDao() {
        return testExecutionDao;
    }

    public TestSettingDao getTestSettingDao() {
        return testSettingDao;
    }

    public TestStandDao getTestStandDao() {
        return testStandDao;
    }

    public ServiceDao getServiceDao() {
        return serviceDao;
    }
}
