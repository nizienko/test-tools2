package ru.yamoney.test.testtools2.teststand.resources.db;

import org.springframework.jdbc.core.RowMapper;
import ru.yamoney.test.testtools2.teststand.resources.ResourceEntity;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by def on 28.05.15.
 */
public class ResourcesMapper implements RowMapper<ResourceEntity> {
    @Override
    public ResourceEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        ResourceEntity resourceEntity = new ResourceEntity();
        resourceEntity.setId(rs.getInt("id"));
        resourceEntity.setData(rs.getString("data"));
        resourceEntity.setType(rs.getString("type"));
        return resourceEntity;
    }
}
