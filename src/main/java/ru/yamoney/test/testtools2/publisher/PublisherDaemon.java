package ru.yamoney.test.testtools2.publisher;

import ru.yamoney.test.testtools2.common.AbstractDaemon;
import ru.yamoney.test.testtools2.common.Application;
import ru.yamoney.test.testtools2.common.ApplicationThread;
import ru.yamoney.test.testtools2.db.DaoContainer;
import ru.yamoney.test.testtools2.testmanager.TestExecution;
import ru.yamoney.test.testtools2.testmanager.TestExecutionSubscriber;
import ru.yamoney.test.testtools2.testmanager.TestManager;

/**
 * Created by def on 05.04.15.
 */
public class PublisherDaemon extends AbstractDaemon implements ApplicationThread, TestExecutionSubscriber {
    private DaoContainer daoContainer;
    private int skippedIterations = 0;
    private boolean hasNewExecutions = true;
    public boolean isSubscribed = false;
    private final int maxSkippedIterations = 10;
    private PublisherWorker publisherWorker;
    public PublisherDaemon(Integer period) {
        super(period);
    }

    public void setDaoContainer(DaoContainer daoContainer) {
        this.daoContainer = daoContainer;
    }


    public void init(){
        publisherWorker = new PublisherWorker(daoContainer);
        Application.addThread(this);
    }

    @Override
    protected void process() {
        subscribe();
        if (hasNewExecutions || skippedIterations > maxSkippedIterations) {
            hasNewExecutions = false;
            skippedIterations = 0;
            LOG.info("Publicating results");
            publisherWorker.work();
        }
        else {
            skippedIterations++;
        }

    }

    public String toString(){
        return "Publisher";
    }

    @Override
    public void addTestExecution(TestExecution testExecution) {
        hasNewExecutions = true;
    }

    private void subscribe(){
        if (!isSubscribed) {
            TestManager testManager = (TestManager) Application.getCtx().getBean("testManager");
            testManager.addSubscriber(this);
            isSubscribed = true;
        }
    }
}
