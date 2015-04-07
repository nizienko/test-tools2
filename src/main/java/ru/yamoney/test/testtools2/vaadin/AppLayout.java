package ru.yamoney.test.testtools2.vaadin;

import com.vaadin.server.ExternalResource;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Link;
import com.vaadin.ui.Table;
import ru.yamoney.test.testtools2.vaadin.testresults.TestResultsLayout;

/**
 * Created by def on 08.04.15.
 */
public class AppLayout extends GridLayout {
    public AppLayout() {
        super(1, 2);
        this.setRowExpandRatio(1, 1);
        this.setSizeFull();
        this.addComponent(new Link("Stop", new ExternalResource("/rest/manage/stop")));
        this.addComponent(new TestResultsLayout());
    }
}
