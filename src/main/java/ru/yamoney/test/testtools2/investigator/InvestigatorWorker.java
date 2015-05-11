package ru.yamoney.test.testtools2.investigator;

import org.apache.log4j.Logger;
import org.omg.PortableInterceptor.LOCATION_FORWARD;
import ru.yamoney.test.testtools2.db.DaoContainer;
import ru.yamoney.test.testtools2.testmanager.ExecutionStatus;
import ru.yamoney.test.testtools2.testmanager.ReasonStatus;
import ru.yamoney.test.testtools2.testmanager.TestExecution;
import ru.yamoney.test.testtools2.vaadin.testresults.TestResultsFilter;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by def on 11.05.15.
 */
public class InvestigatorWorker {
    private DaoContainer daoContainer;
    private TestResultsFilter filter;
    private Date lastCheck;
    public static final Logger LOG = Logger.getLogger(InvestigatorWorker.class);


    public InvestigatorWorker(DaoContainer daoContainer) {
        this.daoContainer = daoContainer;
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -3);
        lastCheck = calendar.getTime();
        filter = new TestResultsFilter();
        filter.setStatus(ExecutionStatus.FAILED.getValue());
        filter.setFailedReason(ReasonStatus.NOT_SET.name());
    }

    public void work() {
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(lastCheck);
        calendar.add(Calendar.HOUR, -8);
        filter.setToDate(now);
        filter.setSinceDate(calendar.getTime());
        lastCheck.setTime(now.getTime());

        for (TestExecution te: daoContainer.getTestExecutionDao().getByFilter(filter)) {
            LOG.info("Investigating " + te.getName());
            daoContainer.getTestExecutionDao().setFailedReason(te, ReasonStatus.UNKNOWN.name());
        }
    }
}
