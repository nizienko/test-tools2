package ru.yamoney.test.testtools2.vaadin.teststand;

import com.vaadin.ui.VerticalLayout;

/**
 * Created by def on 05.07.15.
 */
public class ServicesLayout extends VerticalLayout {

    public ServicesLayout() {
        this.setSizeFull();
        this.setSpacing(false);
        this.addComponent(new ServiceMenu("Services"));
    }
}
