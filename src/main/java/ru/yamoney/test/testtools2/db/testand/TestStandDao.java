package ru.yamoney.test.testtools2.db.testand;

import org.springframework.jdbc.core.JdbcTemplate;
import ru.yamoney.test.testtools2.teststand.ResourceEntity;

import java.util.List;

/**
 * Created by def on 28.05.15.
 */
public class TestStandDao {
    private JdbcTemplate jdbcTemplate;

    public TestStandDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<ResourceEntity> getResources() {
        String SQL = "SELECT id, data, type\n" +
                "  FROM resources where type not in ('DB_ORACLE', 'DB_POSTGRES') order by data->>'name';\n";
        return jdbcTemplate.query(SQL, new ResourcesMapper());
    }

    public List<ResourceEntity> getDataSources() {
        String SQL = "SELECT id, data, type\n" +
                "  FROM resources where type in ('DB_ORACLE', 'DB_POSTGRES');\n";
        return jdbcTemplate.query(SQL, new ResourcesMapper());
    }
}
