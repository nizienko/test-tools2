package ru.yamoney.test.testtools2.vaadin.testsettings.body;


import com.vaadin.ui.VerticalLayout;
import ru.yamoney.test.testtools2.testmanager.TestConfiguration;
import ru.yamoney.test.testtools2.testmanager.TestSetting;

import java.util.List;

/**
 * Created by nizienko on 12.11.14.
 */
public class TestSettingsBodyLayout extends VerticalLayout {
    TestSettingsTable testSettingsTable;

    public TestSettingsBodyLayout() {
        testSettingsTable = new TestSettingsTable();
        this.addComponent(testSettingsTable);
    }

    public void updateTestSettings(TestConfiguration testConfiguration, List<TestSetting> testSettingList) {
        testSettingsTable.updateTestSettings(testConfiguration, testSettingList);
    }

    public void clear() {
        testSettingsTable.clear();
    }
}
