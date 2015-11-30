package ru.yamoney.test.testtools2.vaadin.informer_ui;

import com.vaadin.data.Item;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import org.apache.log4j.Logger;
import org.apache.log4j.or.ThreadGroupRenderer;
import ru.yamoney.test.testtools2.common.Application;
import ru.yamoney.test.testtools2.teststand.TestStand;
import ru.yamoney.test.testtools2.teststand.resources.Resource;

import java.util.Date;

/**
 * Created by def on 30.11.15.
 */
public class InformerTestStandLayout extends VerticalLayout {
    private TestStand testStand;
    private Table table;
    public static final Logger LOG = Logger.getLogger(InformerTestStandLayout.class);

    public InformerTestStandLayout(){
        super();
        this.setSizeFull();
        testStand = (TestStand) Application.getCtx().getBean("testStand");

        table = new Table();
        table.setWidth("100%");
        table.setHeight("100%");
        table.setEditable(false);
        table.setSelectable(true);
        table.setImmediate(true);
        table.addContainerProperty("", Integer.class, null);
        table.addContainerProperty("Name", Resource.class, null);
        table.addContainerProperty("Status", String.class, null);

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
        this.addComponent(table);

    }

    public void update() {
        table.removeAllItems();
        int i = 1;
        for (Resource resource : testStand.getResources()) {
            String status = "offline";
            if (resource.getStatus().isOnline()) {
                status = "online";
            }

            table.addItem(new Object[]{
                    i,
                    resource,
                    status
            }, new Integer(i));
            i++;
        }
    }
}
