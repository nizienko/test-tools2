package ru.yamoney.test.testtools2.info;

import org.apache.log4j.Logger;
import ru.yamoney.test.testtools2.testmanager.TestExecution;
import ru.yamoney.test.testtools2.testmanager.TestExecutionSubscriber;
import ru.yamoney.test.testtools2.testmanager.TestManager;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by def on 28.11.15.
 */
public class Informer implements TestExecutionSubscriber {
    public static final Logger LOG = Logger.getLogger(Informer.class);
    private List<TestExecution> testExecutions;

    public Informer(TestManager testManager){
        testExecutions = new LinkedList<>();
        testManager.addSubscriber(this);
    }

    @Override
    public void addTestExecution(TestExecution testExecution) {
        testExecutions.add(testExecution);
        if (testExecutions.size() > 10) {
            testExecutions.remove(0);
        }
    }

    public List<TestExecution> getExecutions(){
        return testExecutions;
    }
}
