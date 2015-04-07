package ru.yamoney.test.testtools2.testmanager;

/**
 * Created by def on 03.04.15.
 */
public interface TestExecutionNotifier {
    public void addSubscriber(TestExecutionSubscriber testExecutionSubscriber);
}
