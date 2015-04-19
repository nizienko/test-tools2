package ru.yamoney.test.testtools2.vaadin.testresults;

import com.vaadin.data.Property;
import com.vaadin.ui.ListSelect;
import ru.yamoney.test.testtools2.db.DaoContainer;
import ru.yamoney.test.testtools2.testmanager.ExecutionStatus;

/**
 * Created by def on 19.04.15.
 */
public class TestResultsFilterWithStatusLayout extends TestResultsFilterLayout {
    private ListSelect statusSelect;
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
    }


}
