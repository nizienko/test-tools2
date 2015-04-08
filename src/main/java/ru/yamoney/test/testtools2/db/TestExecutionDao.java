package ru.yamoney.test.testtools2.db;

import org.springframework.jdbc.core.JdbcTemplate;
import ru.yamoney.test.testtools2.testmanager.TestExecution;

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
        String SQL = "INSERT INTO test_execution(\n" +
                "            project, version, build, execution, issue, name, status, \n" +
                "            comment, execution_dt)\n" +
                "    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
        jdbcTemplate.update(SQL, new Object[]{
                te.getProject(), te.getVersion(), te.getBuild(), te.getExecution(), te.getIssue(), te.getName(), te.getStatus(),
                te.getComment(), te.getExecutionDt()
        });
    }

    public List<TestExecution> getAll() {
        String SQL = "SELECT id, project, version, build, execution, issue, name, status, \n" +
                "       comment, publicated_status, execution_dt, last_change_dt\n" +
                "  FROM test_execution;\n";
        return jdbcTemplate.query(SQL, new TestExecutionMapper());
    }

    public List<TestExecution> getNotPublished(){
        String SQL = "SELECT id, project, version, build, execution, issue, name, status, \n" +
                "       comment, publicated_status, execution_dt, last_change_dt\n" +
                "  FROM test_execution where publicated_status=0;\n";
        return jdbcTemplate.query(SQL, new TestExecutionMapper());
    }

    public void updateTestExecution(TestExecution te) {
        String SQL = "UPDATE test_execution\n" +
                "   SET project=?, version=?, build=?, execution=?, issue=?, name=?, \n" +
                "       status=?, comment=?, publicated_status=?, execution_dt=?, last_change_dt=? \n" +
                " WHERE id=?";

        jdbcTemplate.update(SQL, new Object[]{
            te.getProject(), te.getVersion(), te.getBuild(), te.getExecution(), te.getIssue(), te.getName(),
                te.getStatus(), te.getComment(), te.isPublicated(), te.getExecutionDt(), te.getLastChangeDt(), te.getId()
        });
    }

}
