package ru.yamoney.test.testtools2.vaadin.testsettings.body;


import com.vaadin.data.Property;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import ru.yamoney.test.testtools2.testmanager.TestConfiguration;
import ru.yamoney.test.testtools2.testmanager.TestSetting;

import java.util.List;

/**
 * Created by nizienko on 12.11.14.
 */
public class TestSettingsTable extends VerticalLayout {
    private TestConfiguration testConfiguration;
    private Table table;

    public TestSettingsTable() {
        this.setSizeFull();
        table = new Table();
        table.setWidth("100%");
        table.setHeight("100%");
        table.setEditable(false);
        table.setSelectable(true);
        table.setImmediate(true);
        table.addContainerProperty("Parameter", String.class, null);
        table.addContainerProperty("Value", String.class, null);
        table.addContainerProperty("Description", String.class, null);
        this.addComponent(table);
        table.addValueChangeListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                try {
                    String parameter = (String) table.getContainerProperty(table.getValue(), "Parameter").getValue();
                    UI.getCurrent().addWindow(new TestSettingsEditWindow(testConfiguration, parameter));
                } catch (NullPointerException e) {
                }
            }
        });
    }

    public void updateTestSettings(TestConfiguration testConfiguration, List<TestSetting> testSettings) {
        table.removeAllItems();
        this.testConfiguration = testConfiguration;
        int i = 1;
        for (TestSetting ts : testSettings) {
            table.addItem(new Object[]{
                    ts.getParameterName(),
                    ts.getValue(),
                    ts.getDescription()
            }, new Integer(i));
            i++;
        }
    }

    public void clear() {
        table.removeAllItems();
        testConfiguration = null;
    }

}


