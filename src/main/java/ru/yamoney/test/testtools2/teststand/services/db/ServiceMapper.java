package ru.yamoney.test.testtools2.teststand.services.db;

import org.springframework.jdbc.core.RowMapper;
import ru.yamoney.test.testtools2.teststand.services.Service;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by def on 05.07.15.
 */
public class ServiceMapper  implements RowMapper<Service> {
    @Override
    public Service mapRow(ResultSet rs, int rowNum) throws SQLException {
        Service service = new Service(rs.getString("data"));
        return service;
    }
}
