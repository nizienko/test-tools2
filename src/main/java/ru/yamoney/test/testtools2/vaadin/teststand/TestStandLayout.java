package ru.yamoney.test.testtools2.vaadin.teststand;


import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.*;
import ru.yamoney.test.testtools2.common.Application;
import ru.yamoney.test.testtools2.teststand.Resource;
import ru.yamoney.test.testtools2.teststand.TestStand;

import java.util.Date;

/**
 * Created by def on 27.05.15.
 */
public class TestStandLayout extends GridLayout {
    private TestStand testStand;
    private Table table;
    private CheckBox editCheckBox;
    private Button updateButton;
    private Button reloadSettingsButton;



    public TestStandLayout() {
        super(1, 2);
        this.setRowExpandRatio(1, 1);
        this.setSizeFull();
        testStand = (TestStand) Application.getCtx().getBean("testStand");
        this.setSizeFull();

        table = new Table();
        table.setWidth("100%");
        table.setHeight("100%");
        table.setEditable(false);
        table.setSelectable(true);
        table.setImmediate(true);
        table.addContainerProperty("", Integer.class, null);
        table.addContainerProperty("Name", Resource.class, null);
        table.addContainerProperty("Status", String.class, null);
        table.addContainerProperty("Comment", String.class, null);
        table.addContainerProperty("Check time", Date.class, null);

        table.setCellStyleGenerator(new Table.CellStyleGenerator() {
            @Override
            public String getStyle(Table source, Object itemId, Object propertyId) {
                if (propertyId != null) {
                    Item item = source.getItem(itemId);
                    String status = (String) item.getItemProperty("Status").getValue();
                    if ("online".equals(status)) {
                        return "passed";
                    } else {
                        return "failed";
                    }
                } else {
                    return null;
                }
            }
        });
        table.addItemClickListener(new ItemClickEvent.ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent event) {
                if (editCheckBox.getValue()) {
                    Resource resource = (Resource) event.getItem().getItemProperty("Name").getValue();
//                    editExecution(te);
                }
            }
        });
//        editCheckBox = new CheckBox("Edit");
        updateButton = new Button("Update");
        updateButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                update();
            }
        });

        reloadSettingsButton = new Button("Reload settings");
        reloadSettingsButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                testStand.loadResources();
                update();
            }
        });

        HorizontalLayout buttonsLayout= new HorizontalLayout();
        buttonsLayout.addComponent(updateButton);
        buttonsLayout.addComponent(editCheckBox);
        buttonsLayout.addComponent(reloadSettingsButton);
        this.addComponent(buttonsLayout);
        this.addComponent(table);

    }

    private void update(){
        table.removeAllItems();
        int i = 1;
        for (Resource resource: testStand.getResources()) {
            String status = "offline";
            if (resource.getStatus().isOnline()) {
                status = "online";
            }

            table.addItem(new Object[]{
                    i,
                    resource,
                    status,
                    resource.getStatus().getStatusMeassage(),
                    resource.getStatus().getLastCheck()
            }, new Integer(i));
            i++;
        }
    }
}
