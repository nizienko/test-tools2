package ru.yamoney.test.testtools2.db;

import org.springframework.jdbc.core.JdbcTemplate;
import ru.yamoney.test.testtools2.testmanager.TestExecution;

import java.util.Date;
import java.util.List;

/**
 * Created by def on 02.04.15.
 */
public class TestExecutionDao {
    private JdbcTemplate jdbcTemplate;

    public TestExecutionDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insert(TestExecution te){
        String SQL = "INSERT INTO execution(\n" +
                "            data, execution_dt, last_change_dt)\n" +
                "    VALUES (?::jsonb, ?, ?);";
        jdbcTemplate.update(SQL, new Object[]{
                te.getData(), te.getExecutionDt(), new Date()
        });
    }

    public List<TestExecution> getAll() {
        String SQL = "SELECT id, data, execution_dt, last_change_dt\n" +
                "  FROM execution;\n";
        return jdbcTemplate.query(SQL, new TestExecutionMapper());
    }

    public List<TestExecution> getNotPublished(){
        String SQL = "SELECT id, data, execution_dt, last_change_dt\n" +
                "  FROM execution where data->>'publicated' = '0';\n";
        return jdbcTemplate.query(SQL, new TestExecutionMapper());
    }

    public void updateTestExecution(TestExecution te) {
        String SQL = "UPDATE execution\n" +
                "   SET data=?::jsonb, execution_dt=?, last_change_dt=? \n" +
                " WHERE id=?";

        jdbcTemplate.update(SQL, new Object[]{
                te.getData(), te.getExecutionDt(), new Date(), te.getId()
        });
    }

}
