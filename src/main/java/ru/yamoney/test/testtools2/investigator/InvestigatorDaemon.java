package ru.yamoney.test.testtools2.investigator;

import ru.yamoney.test.testtools2.common.AbstractDaemon;
import ru.yamoney.test.testtools2.common.Application;
import ru.yamoney.test.testtools2.common.ApplicationThread;
import ru.yamoney.test.testtools2.common.DaoContainer;
import ru.yamoney.test.testtools2.testmanager.TestExecution;
import ru.yamoney.test.testtools2.testmanager.TestExecutionSubscriber;
import ru.yamoney.test.testtools2.testmanager.TestManager;

/**
 * Created by def on 11.05.15.
 */
public class InvestigatorDaemon extends AbstractDaemon implements ApplicationThread, TestExecutionSubscriber {
    private DaoContainer daoContainer;
    private int skippedIterations = 0;
    private boolean hasNewExecutions = true;
    public boolean isSubscribed = false;
    private final int maxSkippedIterations = 360;
    private InvestigatorWorker investigatorWorker;
    public InvestigatorDaemon(Integer period) {
        super(period);
    }

    public void setDaoContainer(DaoContainer daoContainer) {
        this.daoContainer = daoContainer;
    }


    public void init(){
        investigatorWorker = new InvestigatorWorker(daoContainer);
        Application.addThread(this);
    }

    @Override
    protected void process() {
        subscribe();
        if (hasNewExecutions || skippedIterations > maxSkippedIterations) {
            hasNewExecutions = false;
            skippedIterations = 0;
            LOG.info("Investigating failed tests");
            investigatorWorker.work();
        }
        else {
            skippedIterations++;
        }    }

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

    public String toString(){
        return "Investigator";
    }
}
