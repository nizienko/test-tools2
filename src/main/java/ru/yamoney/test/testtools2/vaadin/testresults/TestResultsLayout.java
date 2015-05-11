package ru.yamoney.test.testtools2.vaadin.testresults;

import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import org.springframework.transaction.annotation.Transactional;
import ru.yamoney.test.testtools2.common.Application;
import ru.yamoney.test.testtools2.db.DaoContainer;
import ru.yamoney.test.testtools2.testmanager.ExecutionStatus;
import ru.yamoney.test.testtools2.testmanager.TestExecution;

import java.text.DateFormat;
import java.util.Date;

import static ru.yamoney.test.testtools2.testmanager.ExecutionStatus.*;

/**
 * Created by def on 08.04.15.
 */
public class TestResultsLayout extends GridLayout {
    private Table table;
    private TestResultsFilterWithStatusLayout testResultsFilterLayout;
    private Button updateButton;
    private Button exportButton;

    private DaoContainer daoContainer;

    public TestResultsLayout() {
        super(1, 3);
        this.setRowExpandRatio(2, 1);
        this.setSizeFull();
        daoContainer = (DaoContainer) Application.getCtx().getBean("daoContainer");

        testResultsFilterLayout = new TestResultsFilterWithStatusLayout(daoContainer);
        this.addComponent(testResultsFilterLayout);
        HorizontalLayout buttonsLayout= new HorizontalLayout();
        updateButton = new Button("Update");
        updateButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                update();
            }
        });

        exportButton = new Button("Text");
        exportButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                export();
            }
        });

        buttonsLayout.addComponent(updateButton);
        buttonsLayout.addComponent(exportButton);
        this.addComponent(buttonsLayout);

        table = new Table();
        table.setWidth("100%");
        table.setHeight("100%");
        table.setEditable(false);
        table.setSelectable(true);
        table.setImmediate(true);
        table.addContainerProperty("", Integer.class, null);
        table.addContainerProperty("Component", String.class, null);
        table.addContainerProperty("Issue", String.class, null);
        table.addContainerProperty("Name", TestExecution.class, null);
        table.addContainerProperty("Comment", String.class, null);
        table.addContainerProperty("Date", Date.class, null);
        table.addContainerProperty("Status", String.class, null);
        table.addContainerProperty("Reason", String.class, null);

        table.setCellStyleGenerator(new Table.CellStyleGenerator() {
            @Override
            public String getStyle(Table source, Object itemId, Object propertyId) {
                if (propertyId != null) {
                    Item item = source.getItem(itemId);
                    String status = (String) item.getItemProperty("Status").getValue();
                    if ("passed".equals(status)) {
                        return "passed";
                    } else if ("failed".equals(status)) {
                        return "failed";
                    } else if ("processing".equals(status)) {
                        return "processing";
                    } else {
                        return "not_run";
                    }
                } else {
                    return null;
                }
            }
        });
        this.addComponent(table);

        table.addItemClickListener(new ItemClickEvent.ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent event) {
                TestExecution te = (TestExecution) event.getItem().getItemProperty("Name").getValue();
                editExecution(te);
            }
        });
    }

    private void update(){
        table.removeAllItems();
        int i = 1;
        for (TestExecution te : daoContainer.getTestExecutionDao().getByFilter(testResultsFilterLayout.getFilter())) {
            String status = "failed";
            if (te.getStatus() == 5) {
                status = "passed";
            }
            else if (te.getStatus() == 10){
                status = "processing";
            }
            table.addItem(new Object[]{
                    i,
                    te.getProject(),
                    te.getIssue(),
                    te,
                    te.getComment(),
                    te.getExecutionDt(),
                    status,
                    te.getFailReason()
            }, new Integer(i));
            i++;
        }
    }

    @Transactional
    private void editExecution(TestExecution te1) {
        TestExecution te = daoContainer.getTestExecutionDao().get(te1.getId());
        Window editWindow = new Window(te.toString());
        editWindow.setWidth("700px");
        editWindow.setHeight("600px");
        VerticalLayout layout = new VerticalLayout();

        TextField nameTextField = new TextField("Name");
        nameTextField.setValue(te.getName());
        layout.addComponent(nameTextField);

        TextField issueTextField = new TextField("Issue");
        issueTextField.setValue(te.getIssue());
        layout.addComponent(issueTextField);

        TextField commentTextField = new TextField("Comment");
        commentTextField.setValue(te.getComment());
        layout.addComponent(commentTextField);

        editWindow.center();
        editWindow.setContent(layout);
        UI.getCurrent().addWindow(editWindow);

    }

    private void export(){
        TestResultsFilter filter = testResultsFilterLayout.getFilter();
        Window exportWindow = new Window(filter.toString());
        exportWindow.setWidth("700px");
        exportWindow.setHeight("600px");

        TextArea textLabel = new TextArea();
        textLabel.setSizeFull();
        exportWindow.center();
        exportWindow.setContent(textLabel);
        StringBuffer text = new StringBuffer();
        for (TestExecution te : daoContainer.getTestExecutionDao().getByFilter(testResultsFilterLayout.getFilter())) {
            String status = "failed";
            if (te.getStatus() == PASSED.getIntegerValue()) {
                status = "passed";
            }
            else if (te.getStatus() == PROCESSING.getIntegerValue()){
                status = "processing";
            }
            text.append(te.getIssue() + ": " + te.getName() + " (" + te.getComment() + ") - " + status + "\n");
        }
        textLabel.setValue(text.toString());
        UI.getCurrent().addWindow(exportWindow);
    }
}
