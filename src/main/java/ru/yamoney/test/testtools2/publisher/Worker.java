package ru.yamoney.test.testtools2.publisher;

import org.apache.log4j.Logger;
import ru.yamoney.test.testtools2.db.DaoContainer;
import ru.yamoney.test.testtools2.testmanager.TestExecution;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by def on 06.04.15.
 */
public class Worker {
    private DaoContainer daoContainer;
    public static final Logger LOG = Logger.getLogger(Worker.class);

    public Worker(DaoContainer daoContainer) {
        this.daoContainer = daoContainer;
    }

    public void work() {
        for (TestExecution te: daoContainer.getTestExecutionDao().getNotPublished()) {
            LOG.info(te.getName());
            te.setPublicated(1);
            daoContainer.getTestExecutionDao().updateTestExecution(te);
        }
    }
}
