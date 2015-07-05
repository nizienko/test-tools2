package ru.yamoney.test.testtools2.vaadin.testresults;

import com.vaadin.data.Property;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ListSelect;
import ru.yamoney.test.testtools2.common.DaoContainer;
import ru.yamoney.test.testtools2.testmanager.ExecutionStatus;
import ru.yamoney.test.testtools2.testmanager.ReasonStatus;

/**
 * Created by def on 19.04.15.
 */
public class TestResultsFilterWithStatusLayout extends TestResultsFilterLayout {
    private ListSelect statusSelect;
    private ListSelect reasonSelect;
    private CheckBox acceptanceOnlyCheckBox;

    public TestResultsFilterWithStatusLayout(DaoContainer daoContainer) {
        super(daoContainer);
        statusSelect = new ListSelect("Status");
        statusSelect.setWidth(WIDTH);
        statusSelect.setRows(1);
        statusSelect.addItem("Passed");
        statusSelect.addItem("Failed");
        statusSelect.addItem("Processing");
        statusSelect.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                String status = (String) event.getProperty().getValue();
                if (status == null || "".equals(status)) {
                    filter.setStatus(null);
                }
                else if (status.equals("Passed")){
                    filter.setStatus(ExecutionStatus.PASSED.getValue());
                }
                else if (status.equals("Failed")){
                    filter.setStatus(ExecutionStatus.FAILED.getValue());
                }
                else if (status.equals("Processing")){
                    filter.setStatus(ExecutionStatus.PROCESSING.getValue());
                }
            }
        });
        super.addComponent(statusSelect);

        reasonSelect = new ListSelect("Reason");
        reasonSelect.setWidth(WIDTH);
        reasonSelect.setRows(1);
        reasonSelect.addItem(ReasonStatus.NOT_SET.name());
        reasonSelect.addItem(ReasonStatus.UNKNOWN.name());
        reasonSelect.addItem(ReasonStatus.TEST_STAND.name());
        reasonSelect.addItem(ReasonStatus.BUG.name());

        reasonSelect.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                String status = (String) event.getProperty().getValue();
                if (status == null || "".equals(status)) {
                    filter.setFailedReason(null);
                }
                else if (status.equals("NOT_SET")){
                    filter.setFailedReason(ReasonStatus.NOT_SET.name());
                }
                else if (status.equals("UNKNOWN")){
                    filter.setFailedReason(ReasonStatus.UNKNOWN.name());
                }
                else if (status.equals("TEST_STAND")){
                    filter.setFailedReason(ReasonStatus.TEST_STAND.name());
                }
                else if (status.equals("BUG")){
                    filter.setFailedReason(ReasonStatus.BUG.name());
                }
            }
        });
        super.addComponent(reasonSelect);

        acceptanceOnlyCheckBox = new CheckBox("Acceptance only");
        super.addComponent(acceptanceOnlyCheckBox);
    }

    public TestResultsFilter getFilter(){
        TestResultsFilter testResultsFilter = super.getFilter();
        if (acceptanceOnlyCheckBox.getValue()) {
            testResultsFilter.setAcceptance("1");
        }
        else {
            testResultsFilter.setAcceptance(null);
        }
        return testResultsFilter;
    }
}
