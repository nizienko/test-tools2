package ru.yamoney.test.testtools2.publisher;

import org.apache.log4j.Logger;
import ru.yamoney.test.testtools2.common.DaoContainer;
import ru.yamoney.test.testtools2.testmanager.TestExecution;

/**
 * Created by def on 06.04.15.
 */
public class PublisherWorker {
    public static final Logger LOG = Logger.getLogger(PublisherWorker.class);
    private DaoContainer daoContainer;

    public PublisherWorker(DaoContainer daoContainer) {
        this.daoContainer = daoContainer;
    }

    public void work() {
        for (TestExecution te : daoContainer.getTestExecutionDao().getNotPublished()) {
            LOG.info(te.getName());
            daoContainer.getTestExecutionDao().setPublished(te);
        }
    }
}
