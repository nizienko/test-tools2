package ru.yamoney.test.testtools2.vaadin;

import com.vaadin.ui.TabSheet;
import ru.yamoney.test.testtools2.vaadin.testresults.CompareExecutionsLayout;
import ru.yamoney.test.testtools2.vaadin.testresults.FailedTestsLayout;
import ru.yamoney.test.testtools2.vaadin.manage.ManageLayout;
import ru.yamoney.test.testtools2.vaadin.testresults.TestResultsLayout;
import ru.yamoney.test.testtools2.vaadin.testresults.TotalInfoLayout;
import ru.yamoney.test.testtools2.vaadin.testsettings.TestSettingsLayout;

/**
 * Created by def on 18.04.15.
 */
public class TestToolsTabSheet extends TabSheet {
    public TestToolsTabSheet(){
        this.setSizeFull();
        this.addTab(new TotalInfoLayout(), "8 hours summary");
        this.addTab(new TestResultsLayout(), "Results");
        this.addTab(new FailedTestsLayout(), "Failed tests");
        this.addTab(new CompareExecutionsLayout(), "Compare executions");
        this.addTab(new TestSettingsLayout(), "Settings manager");
        this.addTab(new ManageLayout(), "Manage");
    }
}
