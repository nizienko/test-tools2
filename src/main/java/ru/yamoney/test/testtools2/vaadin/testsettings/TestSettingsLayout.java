package ru.yamoney.test.testtools2.vaadin.testsettings;


import com.vaadin.ui.GridLayout;
import ru.yamoney.test.testtools2.vaadin.testsettings.body.TestSettingsBodyLayout;
import ru.yamoney.test.testtools2.vaadin.testsettings.head.TestSettingsHeadLayout;

/**
 * Created by def on 12.11.14.
 */
public class TestSettingsLayout extends GridLayout {
    TestSettingsHeadLayout testSettingsHeadLayout;
    TestSettingsBodyLayout testSettingsBodyLayout;

    public TestSettingsLayout() {
        super(1, 2);
        this.setRowExpandRatio(1, 1);
        this.setSizeFull();
        testSettingsBodyLayout = new TestSettingsBodyLayout();
        testSettingsHeadLayout = new TestSettingsHeadLayout(testSettingsBodyLayout);
        this.addComponent(testSettingsHeadLayout);
        this.addComponent(testSettingsBodyLayout);
        this.setSizeFull();
        testSettingsBodyLayout.setSizeFull();

    }
}
