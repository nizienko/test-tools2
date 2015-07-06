package ru.yamoney.test.testtools2.teststand.services.db;

import org.springframework.jdbc.core.JdbcTemplate;
import ru.yamoney.test.testtools2.teststand.services.Service;

import java.util.List;

/**
 * Created by def on 05.07.15.
 */
public class ServiceDao {
    private JdbcTemplate jdbcTemplate;

    public ServiceDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Service> getServices() {
        String SQL = "SELECT id, data\n" +
                "  FROM services;\n";
        return jdbcTemplate.query(SQL, new ServiceMapper());
    }
}
