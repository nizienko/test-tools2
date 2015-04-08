package ru.yamoney.test.testtools2.vaadin.testresults;

import com.vaadin.data.Item;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import ru.yamoney.test.testtools2.common.Application;
import ru.yamoney.test.testtools2.db.DaoContainer;
import ru.yamoney.test.testtools2.testmanager.TestExecution;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by def on 08.04.15.
 */
public class TestResultsLayout extends VerticalLayout {
    private Table table;
    private DateFormat dateFormat;

    public TestResultsLayout(){
        this.setSizeFull();
        table = new Table();
        table.setWidth("100%");
        table.setHeight("100%");
        table.setEditable(false);
        table.setSelectable(true);
        table.setImmediate(true);
        table.addContainerProperty("", Integer.class, null);
        table.addContainerProperty("Component", String.class, null);
        table.addContainerProperty("Issue", String.class, null);
        table.addContainerProperty("Name", String.class, null);
        table.addContainerProperty("Comment", String.class, null);
        table.addContainerProperty("Date", Date.class, null);
        table.addContainerProperty("Status", String.class, null);
        table.setCellStyleGenerator(new Table.CellStyleGenerator() {
            @Override
            public String getStyle(Table source, Object itemId, Object propertyId) {
                if(propertyId != null ) {
                    Item item = source.getItem(itemId);
                    String status = (String) item.getItemProperty("Status").getValue();
                    if ("passed".equals(status)){
                        return "passed";
                    }
                    else if ("failed".equals(status)){
                        return "failed";
                    }
                    else {
                        return "not_run";
                    }
                }
                else {
                    return null;
                }
            }
        });
        this.addComponent(table);

        DaoContainer daoContainer = (DaoContainer) Application.getCtx().getBean("daoContainer");

        int i = 1;
        for (TestExecution te : daoContainer.getTestExecutionDao().getAll()) {
            String status = "failed";
            if (te.getStatus() == 5) {
                status = "passed";
            }
            table.addItem(new Object[]{
                    i,
                    te.getProject(),
                    te.getIssue(),
                    te.getName(),
                    te.getComment(),
                    te.getExecutionDt(),
                    status
            }, new Integer(i));
            i++;
        }
    }
}
