package ru.yamoney.test.testtools2.teststand.resources;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.postgresql.jdbc3.Jdbc3PoolingDataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yamoney.test.testtools2.common.DaoContainer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by def on 06.06.15.
 */
public class JdbcTemplateContainer {
    public static final Logger LOG = Logger.getLogger(JdbcTemplateContainer.class);

    private Map<String, JdbcTemplate> jdbcTemplates;

    public JdbcTemplateContainer(DaoContainer daoContainer) {
        jdbcTemplates = new HashMap<>();
    }

    public void addOracleDataSource(String data) {
        try {
            JSONObject dataJSON = new JSONObject(data);
            BasicDataSource source = new BasicDataSource();
            source.setDriverClassName("oracle.jdbc.driver.OracleDriver");
            String url = "jdbc:oracle:thin:@" + dataJSON.get("serverName") + ":" + dataJSON.get("portNumber") +
                    "/" + dataJSON.get("serviceName");
            LOG.info(url);
            source.setUrl(url);
            source.setUsername((String) dataJSON.get("user"));
            source.setPassword((String) dataJSON.get("password"));
            JdbcTemplate jdbcTemplate = new JdbcTemplate(source);
            String name = (String) dataJSON.get("serverName");
            try {
                name = (String) dataJSON.get("name");
            } catch (JSONException e) {
                LOG.error(e.getMessage());
            }
            jdbcTemplates.put(name, jdbcTemplate);
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
    }

    public void addPostgresDataSource(String data) {
        try {
            JSONObject dataJSON = new JSONObject(data);
            Jdbc3PoolingDataSource source = new Jdbc3PoolingDataSource();
            source.setServerName((String) dataJSON.get("serverName"));
            source.setPortNumber(Integer.parseInt((String) dataJSON.get("portNumber")));
            source.setDatabaseName((String) dataJSON.get("databaseName"));
            source.setUser((String) dataJSON.get("user"));
            source.setPassword((String) dataJSON.get("password"));
            source.setMaxConnections(10);
            JdbcTemplate jdbcTemplate = new JdbcTemplate(source);
            String name = (String) dataJSON.get("serverName");
            try {
                name = (String) dataJSON.get("name");
            } catch (JSONException e) {
                LOG.error(e.getMessage());
            }
            jdbcTemplates.put(name, jdbcTemplate);
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
    }

    public Map<String, JdbcTemplate> getJdbcTemplates() {
        return jdbcTemplates;
    }
}
