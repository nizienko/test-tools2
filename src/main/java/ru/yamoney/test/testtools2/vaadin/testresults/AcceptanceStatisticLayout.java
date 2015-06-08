package ru.yamoney.test.testtools2.vaadin.testresults;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import ru.yamoney.test.testtools2.common.Application;
import ru.yamoney.test.testtools2.db.DaoContainer;
import ru.yamoney.test.testtools2.testmanager.ExecutionStatus;
import ru.yamoney.test.testtools2.testmanager.ReasonStatus;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

/**
 * Created by def on 26.05.15.
 */
public class AcceptanceStatisticLayout extends GridLayout{
    private Label label;
    private DaoContainer daoContainer;
    private Button updateButton;
    private TestResultsFilterLayout filterLayout;

    public AcceptanceStatisticLayout(){
        super(1, 3);
        this.setRowExpandRatio(2, 1);
        this.setSizeFull();
        daoContainer = (DaoContainer) Application.getCtx().getBean("daoContainer");
        filterLayout = new TestResultsFilterLayout(daoContainer);
        filterLayout.getFilter().setAcceptance("1");
        updateButton = new Button("Load");
        updateButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                updatePage();
            }
        });

        label = new Label();
        label.setContentMode(ContentMode.HTML);
        this.addComponent(filterLayout);
        this.addComponent(updateButton);
        this.addComponent(label);
    }

    private void updatePage(){
        StringBuffer out = new StringBuffer();
        for (Map<String, Object> p: daoContainer.getTestExecutionDao().selectProjects(filterLayout.getFilter())) {
            String project = (String) p.get("project");
            out.append("<h2>" + project + "</h2>");
            filterLayout.getFilter().setProject(project);
            int total = getCount();
            out.append("Total executions: " + total + ". ");
            filterLayout.getFilter().setStatus(ExecutionStatus.PASSED.getValue());
            int passed = getCount();
            out.append("Passed: " + passed + "(" + getPercentage(passed, total) + ").<br>");
            filterLayout.getFilter().setStatus(ExecutionStatus.FAILED.getValue());
            int failed = getCount();
            out.append("Failed: " + failed + "(" + getPercentage(failed, total) + "). ");
            filterLayout.getFilter().setFailedReason(ReasonStatus.UNKNOWN.name());
            int failedUnknown = getCount();
            out.append("Unknown: " + failedUnknown + "(" + getPercentage(failedUnknown, total) + "). ");
            filterLayout.getFilter().setFailedReason(ReasonStatus.TEST_STAND.name());
            int failedTestStand = getCount();
            out.append("Test stand: " + failedUnknown + "(" + getPercentage(failedTestStand, total) + "). ");
            filterLayout.getFilter().setFailedReason(ReasonStatus.TEST_STAND.name());
            int failedbug = getCount();
            out.append("Bug: " + failedbug + "(" + getPercentage(failedbug, total) + "). ");

            filterLayout.getFilter().setFailedReason(null);
            filterLayout.getFilter().setStatus(null);
            filterLayout.getFilter().setProject(null);
        }
        label.setValue(out.toString());
    }

    private int getCount(){
        return daoContainer.getTestExecutionDao().countByFilter(filterLayout.getFilter());
    }

    private String getPercentage(int y, int z){

        return new BigDecimal(y).setScale(4).divide(new BigDecimal(z).setScale(4), RoundingMode.HALF_UP).setScale(8)
                .multiply(new BigDecimal(100)).setScale(2).toPlainString() + "%";
    }
}
