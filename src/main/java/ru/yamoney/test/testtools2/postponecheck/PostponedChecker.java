package ru.yamoney.test.testtools2.postponecheck;

import org.apache.log4j.Logger;
import ru.yamoney.test.testtools2.common.DaoContainer;
import ru.yamoney.test.testtools2.testmanager.ExecutionStatus;
import ru.yamoney.test.testtools2.testmanager.TestExecution;
import ru.yamoney.test.testtools2.teststand.TestStand;

import java.util.List;

/**
 * Created by def on 08.09.15.
 */
public class PostponedChecker {
    public static final Logger LOG = Logger.getLogger(PostponedChecker.class);

    private DaoContainer daoContainer;

    public PostponedChecker(DaoContainer daoContainer){
        this.daoContainer = daoContainer;
    }

    public void work(){
        List<TestExecution> testExecutions = daoContainer.getTestExecutionDao().getProcessing();
        for (TestExecution testExecution : testExecutions) {
            if (testExecution.getPostponedCheckList() != null) {
                int passedChecks = 0;
                int processingChecks = 0;
                int failedChecks = 0;
                StringBuffer comment = new StringBuffer();
                for (PostponedCheck postponedCheck : testExecution.getPostponedCheckList()) {
                    LOG.info(postponedCheck.getJSON());
                    postponedCheck.process();
                    if (postponedCheck.getStatus() == ExecutionStatus.PASSED.getIntegerValue()) {
                        passedChecks++;
                    }
                    else if (postponedCheck.getStatus() == ExecutionStatus.PROCESSING.getIntegerValue()) {
                        processingChecks++;
                        comment.append("waiting: " + postponedCheck.getDescription() + "; ");
                    }
                    else if (postponedCheck.getStatus() == ExecutionStatus.FAILED.getIntegerValue()) {
                        failedChecks++;
                        comment.append("failed: " + postponedCheck.getDescription() + "; ");
                    }
                    comment.append("passed " + passedChecks + " checks");
                }
                daoContainer.getTestExecutionDao().updatePostponedChecks(testExecution.getId(), testExecution.getPostponedCheckList());
                daoContainer.getTestExecutionDao().setComment(testExecution.getId(), comment.toString());
                if (failedChecks > 0) {
                    daoContainer.getTestExecutionDao().setStatus(testExecution.getId(), ExecutionStatus.FAILED);
                }
                else if (processingChecks > 0 ) {
                    // leave processing status to do other checks in future
                }
                else {
                    daoContainer.getTestExecutionDao().setStatus(testExecution.getId(), ExecutionStatus.PASSED);
                }
            }
            else {
                LOG.info("Nothing to check");
                daoContainer.getTestExecutionDao().setStatus(testExecution.getId(), ExecutionStatus.PASSED);
            }
        }
    }
}
