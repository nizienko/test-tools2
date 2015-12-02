package ru.yamoney.test.testtools2.testmanager;

import ru.yamoney.test.testtools2.common.DaoContainer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by def on 02.04.15.
 */
public class TestManager implements TestExecutionNotifier {
    private DaoContainer daoContainer;
    private List<TestExecutionSubscriber> subscribers;

    public TestManager(DaoContainer daoContainer) {
        this.daoContainer = daoContainer;
        subscribers = new ArrayList<>();
    }

    public synchronized void addTestExecution(TestExecution testExecution) {
        // Save execution in db
        daoContainer.getTestExecutionDao().insert(testExecution);
        // Notifying subscribers
        for (TestExecutionSubscriber tes : subscribers) {
            tes.addTestExecution(testExecution);
        }
    }

    @Override
    public void addSubscriber(TestExecutionSubscriber testExecutionSubscriber) {
        System.out.println("new Subscriber " + testExecutionSubscriber);
        subscribers.add(testExecutionSubscriber);
    }
}
