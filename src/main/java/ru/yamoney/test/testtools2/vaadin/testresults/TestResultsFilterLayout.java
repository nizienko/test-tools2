package ru.yamoney.test.testtools2.vaadin.testresults;

import com.vaadin.data.Property;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.ListSelect;
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
    private DateField sinceDate;
    private DateField toDate;
    private DaoContainer daoContainer;
    private TestResultsFilter filter = new TestResultsFilter();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public TestResultsFilterLayout(DaoContainer daoContainer) {
        this.daoContainer = daoContainer;

        // date
        sinceDate = new DateField();
        toDate = new DateField();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 8);
        toDate.setValue(calendar.getTime());
        calendar.add(Calendar.HOUR, -24);
        sinceDate.setValue(calendar.getTime());
        sinceDate.setResolution(Resolution.MINUTE);
        toDate.setResolution(Resolution.MINUTE);
        filter.setSinceDate(sinceDate.getValue());
        filter.setToDate(toDate.getValue());


        // component/version/build/execution
        projectSelect = new ListSelect("Project");
        projectSelect.setRows(1);
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
        versionSelect.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                filter.setVersion((String) event.getProperty().getValue());
            }
        });
        this.addComponent(versionSelect);
        this.addComponent(sinceDate);
        this.addComponent(toDate);

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

    public TestResultsFilter getFilter(){
        return filter;
    }
}
