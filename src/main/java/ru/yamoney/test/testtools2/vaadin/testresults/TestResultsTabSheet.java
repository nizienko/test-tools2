package ru.yamoney.test.testtools2.vaadin.testresults;

import com.vaadin.ui.TabSheet;

/**
 * Created by def on 18.04.15.
 */
public class TestResultsTabSheet extends TabSheet {
    public TestResultsTabSheet(){
        this.setSizeFull();
        this.addTab(new TestResultsLayout(), "Results");
        this.addTab(new FailedTestsLayout(), "Failed tests");
        this.addTab(new CompareExecutionsLayout(), "Compare executions");
    }
}
