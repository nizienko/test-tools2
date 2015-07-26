package ru.yamoney.test.testtools2.vaadin;

import com.vaadin.ui.GridLayout;
import ru.yamoney.test.testtools2.vaadin.teststand.ServiceMenu;

/**
 * Created by def on 08.04.15.
 */
public class AppLayout extends GridLayout {
    public AppLayout() {
        super(1, 2);
        this.setRowExpandRatio(1, 1);
        this.setSizeFull();
//        this.addComponent(new Label("<h2>Test tools</h2>", ContentMode.HTML));
        this.addComponent(new ServiceMenu());
        this.addComponent(new TestToolsTabSheet());
    }
}
