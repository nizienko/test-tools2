package ru.yamoney.test.testtools2.teststand;

import org.apache.log4j.Logger;
import ru.yamoney.test.testtools2.common.DaoContainer;
import ru.yamoney.test.testtools2.teststand.resources.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by def on 27.05.15.
 */
public class TestStand {
    public static final Logger LOG = Logger.getLogger(TestStand.class);

    private DaoContainer daoContainer;
    private List<Resource> resources;
    private JdbcTemplateContainer jdbcTemplateContainer;

    public TestStand(DaoContainer daoContainer) {
        this.daoContainer = daoContainer;
        loadDataSources();
        resources = null;
    }

    public void loadDataSources(){
        jdbcTemplateContainer = new JdbcTemplateContainer(daoContainer);
        for (ResourceEntity resourceEntity: this.daoContainer.getTestStandDao().getDataSources()) {
            LOG.info(resourceEntity.getType());
            if (resourceEntity.getType().equals(ResourceType.DB_ORACLE.name())) {
                jdbcTemplateContainer.addOracleDataSource(resourceEntity.getData());
            }
            else if (resourceEntity.getType().equals(ResourceType.DB_POSTGRES.name())) {
                jdbcTemplateContainer.addPostgresDataSource(resourceEntity.getData());
            }
        }
    }

    public List<Resource> getResources(){
        if (resources == null) {
            loadResources();
        }
        return resources;
    }



    public void loadResources(){
        ResourceFactory resourceFactory = new ResourceFactory();
        resources = new ArrayList<>();
        for(ResourceEntity resourceEntity: this.daoContainer.getTestStandDao().getResources()) {
            LOG.info(resourceEntity.getType());
            Resource resource = resourceFactory.getResource(resourceEntity.getType());
            resource.init(resourceEntity.getData());
            resources.add(resource);
        }
    }

    public JdbcTemplateContainer getJdbcTemplateContainer(){
        return jdbcTemplateContainer;
    }
}
