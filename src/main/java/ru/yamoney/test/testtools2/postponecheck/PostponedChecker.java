package ru.yamoney.test.testtools2.postponecheck;

import org.apache.log4j.Logger;
import ru.yamoney.test.testtools2.common.DaoContainer;
import ru.yamoney.test.testtools2.testmanager.ExecutionStatus;
import ru.yamoney.test.testtools2.testmanager.TestExecution;
import ru.yamoney.test.testtools2.testmanager.TestExecutionNotifier;
import ru.yamoney.test.testtools2.testmanager.TestExecutionSubscriber;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by def on 08.09.15.
 */
public class PostponedChecker implements TestExecutionNotifier {
    public static final Logger LOG = Logger.getLogger(PostponedChecker.class);
    private List<TestExecutionSubscriber> subscribers;

    private final DaoContainer daoContainer;

    public PostponedChecker(DaoContainer daoContainer) {
        this.daoContainer = daoContainer;
        subscribers = new LinkedList<>();
    }

    public void work() {
        List<TestExecution> testExecutions = daoContainer.getTestExecutionDao().getProcessing();
        for (TestExecution testExecution : testExecutions) {
            if (testExecution.getPostponedCheckList() != null) {
                int passedChecks = 0;
                int processingChecks = 0;
                int failedChecks = 0;
                final StringBuffer comment = new StringBuffer();
                for (PostponedCheck postponedCheck : testExecution.getPostponedCheckList()) {
                    LOG.info(postponedCheck.getJSON());
                    postponedCheck.process();
                    if (postponedCheck.getStatus() == ExecutionStatus.PASSED.getIntegerValue()) {
                        passedChecks++;
                    } else if (postponedCheck.getStatus() == ExecutionStatus.PROCESSING.getIntegerValue()) {
                        processingChecks++;
                        comment.append("waiting: " + postponedCheck.getDescription() + "; ");
                    } else if (postponedCheck.getStatus() == ExecutionStatus.FAILED.getIntegerValue()) {
                        failedChecks++;
                        comment.append("failed: " + postponedCheck.getDescription() + "; ");
                    }
                }
                comment.append("passed " + passedChecks + " postponed checks;");
                daoContainer.getTestExecutionDao().updatePostponedChecks(testExecution.getId(), testExecution.getPostponedCheckList());
                daoContainer.getTestExecutionDao().setComment(testExecution.getId(), comment.toString());
                if (failedChecks > 0) {
                    daoContainer.getTestExecutionDao().setStatus(testExecution.getId(), ExecutionStatus.FAILED);
                    testExecution.setStatus(ExecutionStatus.FAILED.getIntegerValue());
                } else if (processingChecks > 0) {
                    // leave processing status to do other checks in future
                } else {
                    daoContainer.getTestExecutionDao().setStatus(testExecution.getId(), ExecutionStatus.PASSED);
                    testExecution.setStatus(ExecutionStatus.PASSED.getIntegerValue());
                }
            } else {
                daoContainer.getTestExecutionDao().setComment(testExecution.getId(), "Postponed check skipped");
                daoContainer.getTestExecutionDao().setStatus(testExecution.getId(), ExecutionStatus.PASSED);
                testExecution.setStatus(ExecutionStatus.PASSED.getIntegerValue());
            }
            testExecution = daoContainer.getTestExecutionDao().get(testExecution.getId());
            // Notifying subscribers
            for (TestExecutionSubscriber tes : subscribers) {
                tes.addTestExecution(testExecution);
            }
        }
    }

    @Override
    public void addSubscriber(TestExecutionSubscriber testExecutionSubscriber) {
        System.out.println("new Subscriber " + testExecutionSubscriber);
        subscribers.add(testExecutionSubscriber);
    }
}
