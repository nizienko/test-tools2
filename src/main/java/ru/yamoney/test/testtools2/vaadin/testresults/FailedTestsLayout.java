package ru.yamoney.test.testtools2.vaadin.testresults;

import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Table;
import org.apache.log4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;
import ru.yamoney.test.testtools2.common.Application;
import ru.yamoney.test.testtools2.common.DaoContainer;
import ru.yamoney.test.testtools2.testmanager.ExecutionStatus;
import ru.yamoney.test.testtools2.testmanager.TestExecution;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by def on 18.04.15.
 */
public class FailedTestsLayout extends GridLayout {
    private Table table;
    private TestResultsFilterLayout testResultsFilterLayout;
    private Button updateButton;
    private DaoContainer daoContainer;
    public static final Logger LOG = Logger.getLogger(FailedTestsLayout.class);


    public FailedTestsLayout() {
        super(1, 3);
        this.setRowExpandRatio(2, 1);
        this.setSizeFull();
        daoContainer = (DaoContainer) Application.getCtx().getBean("daoContainer");

        testResultsFilterLayout = new TestResultsFilterLayout(daoContainer);
        this.addComponent(testResultsFilterLayout);
        updateButton = new Button("Update");
        updateButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                update();
            }
        });
        this.addComponent(updateButton);
        table = new Table();
        table.setWidth("100%");
        table.setHeight("100%");
        table.setEditable(false);
        table.setSelectable(true);
        table.setImmediate(true);
        table.addContainerProperty("", Integer.class, null);
        table.addContainerProperty("Issue", String.class, null);
        table.addContainerProperty("Name", String.class, null);
        table.addContainerProperty("Fails count", Integer.class, null);
        table.addContainerProperty("Last success date", Date.class, null);
        table.addContainerProperty("Last success version", String.class, null);
        this.addComponent(table);
    }

    private void update(){
        table.removeAllItems();
        List<String> issues = new ArrayList();
        for (Map<String, Object> p: daoContainer.getTestExecutionDao().selectIssues(testResultsFilterLayout.getFilter())) {
            issues.add((String) p.get("issue"));
        }
        int i = 1;
        for (String issue: issues) {
            testResultsFilterLayout.getFilter().setIssueTemp(issue);
            testResultsFilterLayout.getFilter().setStatus(ExecutionStatus.FAILED.getValue());
            List<TestExecution> failedTests = daoContainer.getTestExecutionDao().getByFilter(testResultsFilterLayout.getFilter());
            if (failedTests.size() > 0) {
                testResultsFilterLayout.getFilter().setStatus(ExecutionStatus.PASSED.getValue());
                List<TestExecution> passedTests = daoContainer.getTestExecutionDao().getByFilter(testResultsFilterLayout.getFilter());
                if (passedTests.size() == 0) {
                    TestExecution lastSuccessTest = null;
                    try {
                        lastSuccessTest = daoContainer.getTestExecutionDao().getLastPassed(issue);

                    }
                    catch (EmptyResultDataAccessException e){
                        lastSuccessTest = new TestExecution();
                    }
                    try {
                    table.addItem(new Object[]{
                            i,
                            issue,
                            failedTests.get(0).getName(),
                            failedTests.size(),
                            lastSuccessTest.getExecutionDt(),
                            lastSuccessTest.getProject() + "." + lastSuccessTest.getVersion() + "." + lastSuccessTest.getBuild() + "." + lastSuccessTest.getExecution()
                    }, new Integer(i));
                    i++;
                    }
                    catch (NullPointerException e) {
                        LOG.error("Failed load test execution " + issue + "\n" + e.getMessage());
                    }
                }
            }
            testResultsFilterLayout.getFilter().setStatus(null);
            testResultsFilterLayout.getFilter().setIssueTemp(null);
        }
    }


}
