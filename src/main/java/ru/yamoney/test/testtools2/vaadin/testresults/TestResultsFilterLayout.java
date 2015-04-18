package ru.yamoney.test.testtools2.vaadin.testresults;

import com.vaadin.data.Property;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.*;
import ru.yamoney.test.testtools2.db.DaoContainer;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

/**
 * Created by def on 16.04.15.
 */
public class TestResultsFilterLayout extends HorizontalLayout {
    private ListSelect projectSelect;
    private ListSelect versionSelect;
    private ListSelect buildSelect;
    private ListSelect executionSelect;
    private TextField issueTextField;
    private DateField sinceDate;
    private DateField toDate;
    private DaoContainer daoContainer;
    private TestResultsFilter filter = new TestResultsFilter();
    private final String WIDTH = "90";
    public TestResultsFilterLayout(DaoContainer daoContainer) {
        this.daoContainer = daoContainer;

        // date
        sinceDate = new DateField("Since");
        toDate = new DateField("To");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 8);
        toDate.setValue(calendar.getTime());
        calendar.add(Calendar.HOUR, -24);
        sinceDate.setValue(calendar.getTime());
        sinceDate.setResolution(Resolution.MINUTE);
        toDate.setResolution(Resolution.MINUTE);
        filter.setSinceDate(sinceDate.getValue());
        filter.setToDate(toDate.getValue());
        this.addComponent(sinceDate);
        this.addComponent(toDate);

        // component/version/build/execution
        projectSelect = new ListSelect("Project");
        projectSelect.setRows(1);
        projectSelect.setWidth(WIDTH);
        projectSelect.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                filter.setProject((String) event.getProperty().getValue());
                updateVersionSelect();
            }
        });
        updateProjectSelect();

        this.addComponent(projectSelect);

        versionSelect = new ListSelect("Version");
        versionSelect.setRows(1);
        versionSelect.setWidth(WIDTH);
        versionSelect.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                filter.setVersion((String) event.getProperty().getValue());
                updateBuildSelect();
            }
        });
        this.addComponent(versionSelect);

        buildSelect = new ListSelect("Build");
        buildSelect.setRows(1);
        buildSelect.setWidth(WIDTH);
        buildSelect.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                filter.setBuild((String) event.getProperty().getValue());
                updateExecutionSelect();

            }
        });
        this.addComponent(buildSelect);

        executionSelect = new ListSelect("Execution");
        executionSelect.setRows(1);
        executionSelect.setWidth(WIDTH);
        executionSelect.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                filter.setExecution((String) event.getProperty().getValue());
            }
        });
        this.addComponent(executionSelect);
        issueTextField = new TextField("Issue");
        issueTextField.setWidth(WIDTH);
        this.addComponent(issueTextField);
    }

    private void updateProjectSelect(){
        projectSelect.removeAllItems();
        for (Map<String, Object> p: daoContainer.getTestExecutionDao().selectProjects()) {
            projectSelect.addItem(p.get("project"));
        }
    }

    private void updateVersionSelect(){
        versionSelect.removeAllItems();
        for (Map<String, Object> p: daoContainer.getTestExecutionDao().selectVersions(filter)) {
            versionSelect.addItem(p.get("version"));
        }
    }

    private void updateBuildSelect(){
        buildSelect.removeAllItems();
        for (Map<String, Object> p: daoContainer.getTestExecutionDao().selectBuilds(filter)) {
            buildSelect.addItem(p.get("build"));
        }
    }

    private void updateExecutionSelect(){
        executionSelect.removeAllItems();
        for (Map<String, Object> p: daoContainer.getTestExecutionDao().selectExecutions(filter)) {
            executionSelect.addItem(p.get("execution"));
        }
    }

    public TestResultsFilter getFilter(){
        filter.setSinceDate(sinceDate.getValue());
        filter.setToDate(toDate.getValue());
        filter.setIssue(issueTextField.getValue());
        return filter;
    }
}
