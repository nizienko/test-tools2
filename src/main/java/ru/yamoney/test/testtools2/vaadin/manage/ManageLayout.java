package ru.yamoney.test.testtools2.vaadin.manage;

import com.vaadin.server.ExternalResource;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Link;
import com.vaadin.ui.VerticalLayout;

/**
 * Created by def on 18.04.15.
 */
public class ManageLayout extends VerticalLayout {
    public ManageLayout(){
        this.setSizeFull();
        this.addComponent(new Link("Stop", new ExternalResource("/rest/manage/stop")));
    }
}
