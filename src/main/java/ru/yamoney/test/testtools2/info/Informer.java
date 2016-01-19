package ru.yamoney.test.testtools2.info;

import org.apache.log4j.Logger;
import ru.yamoney.test.testtools2.postponecheck.PostponedChecker;
import ru.yamoney.test.testtools2.testmanager.ExecutionStatus;
import ru.yamoney.test.testtools2.testmanager.TestExecution;
import ru.yamoney.test.testtools2.testmanager.TestExecutionSubscriber;
import ru.yamoney.test.testtools2.testmanager.TestManager;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by def on 28.11.15.
 */
public class Informer implements TestExecutionSubscriber {
    public static final Logger LOG = Logger.getLogger(Informer.class);
    private List<TestExecution> testExecutions;

    public Informer(TestManager testManager){
        testExecutions = Collections.synchronizedList(new LinkedList<>());
        testManager.addSubscriber(this);
    }

    public void setPostponedChecker(PostponedChecker postponedChecker) {
        postponedChecker.addSubscriber(this);
    }

    @Override
    public void addTestExecution(TestExecution testExecution) {
        if (testExecution.getStatus() != ExecutionStatus.PROCESSING.getIntegerValue()) {
            testExecutions.add(testExecution);
            if (testExecutions.size() > 100) {
                testExecutions.remove(0);
            }
        }
    }

    public synchronized List<TestExecution> getExecutions(TestExecution lastExecution){
        if (lastExecution != null) {
            int lastIndex = testExecutions.indexOf(lastExecution);
            if (lastIndex >= 0)
                return new LinkedList<TestExecution>(testExecutions.subList(lastIndex + 1, testExecutions.size()));
        }
        return testExecutions;
    }
}
