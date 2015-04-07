package ru.yamoney.test.testtools2.db;

import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Created by def on 02.04.15.
 */
public class DaoContainer {
    private TestExecutionDao testExecutionDao;

    public DaoContainer(JdbcTemplate jdbcTemplate) {
        testExecutionDao = new TestExecutionDao(jdbcTemplate);
    }

    public TestExecutionDao getTestExecutionDao() {
        return testExecutionDao;
    }
}
