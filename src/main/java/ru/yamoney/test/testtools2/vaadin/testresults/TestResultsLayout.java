package ru.yamoney.test.testtools2.vaadin.testresults;

import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import org.springframework.transaction.annotation.Transactional;
import ru.yamoney.test.testtools2.common.Application;
import ru.yamoney.test.testtools2.common.DaoContainer;
import ru.yamoney.test.testtools2.testmanager.ReasonStatus;
import ru.yamoney.test.testtools2.testmanager.TestExecution;

import java.util.Date;

import static ru.yamoney.test.testtools2.testmanager.ExecutionStatus.PASSED;
import static ru.yamoney.test.testtools2.testmanager.ExecutionStatus.PROCESSING;

/**
 * Created by def on 08.04.15.
 */
public class TestResultsLayout extends GridLayout {
    private Table table;
    private TestResultsFilterWithStatusLayout testResultsFilterLayout;
    private Button updateButton;
    private Button exportButton;
    private CheckBox investigateCheckBox;
    private DaoContainer daoContainer;

    public TestResultsLayout() {
        super(1, 3);
        this.setRowExpandRatio(2, 1);
        this.setSizeFull();
        daoContainer = (DaoContainer) Application.getCtx().getBean("daoContainer");

        testResultsFilterLayout = new TestResultsFilterWithStatusLayout(daoContainer);
        this.addComponent(testResultsFilterLayout);
        HorizontalLayout buttonsLayout = new HorizontalLayout();
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

        investigateCheckBox = new CheckBox("Edit");
        investigateCheckBox.setValue(false);

        buttonsLayout.addComponent(updateButton);
        buttonsLayout.addComponent(exportButton);
        buttonsLayout.addComponent(investigateCheckBox);

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
                if (investigateCheckBox.getValue()) {
                    TestExecution te = (TestExecution) event.getItem().getItemProperty("Name").getValue();
                    editExecution(te);
                }
            }
        });
    }

    private void update() {
        table.removeAllItems();
        int i = 1;
        for (TestExecution te : daoContainer.getTestExecutionDao().getByFilter(testResultsFilterLayout.getFilter())) {
            String status = "failed";
            if (te.getStatus() == 5) {
                status = "passed";
            } else if (te.getStatus() == 10) {
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

    private void editExecution(TestExecution te1) {
        final TestExecution te = daoContainer.getTestExecutionDao().get(te1.getId());
        final Window editWindow = new Window(te.toString());
        final String WIDTH = "250";

        editWindow.setWidth("255px");
        editWindow.setHeight("350px");
        final VerticalLayout layout = new VerticalLayout();

        final Label label = new Label();
        label.setContentMode(ContentMode.HTML);
        StringBuffer sb = new StringBuffer();
        sb.append("<h2>" + te.getExecutionDt() + "</h2>");
        sb.append(te.getProject() + " ");
        sb.append(te.getVersion() + " ");
        sb.append(te.getBuild() + "<br>");
        sb.append("Status: " + te.getStatus() + "<br>");
        if (te.isPublicated() == 1) {
            sb.append("Published<br>");
        } else {
            sb.append("Not published<br>");
        }
        label.setValue(sb.toString());
        layout.addComponent(label);

        final TextField commentTextField = new TextField("Comment");
        commentTextField.setWidth(WIDTH);
        commentTextField.setValue(te.getComment());
        layout.addComponent(commentTextField);

        final ListSelect reasonSelect = new ListSelect("Reason");
        reasonSelect.setWidth(WIDTH);
        reasonSelect.setRows(1);
        reasonSelect.addItem(ReasonStatus.NOT_SET.name());
        reasonSelect.addItem(ReasonStatus.UNKNOWN.name());
        reasonSelect.addItem(ReasonStatus.TEST_STAND.name());
        reasonSelect.addItem(ReasonStatus.BUG.name());
        reasonSelect.setValue(te.getFailReason());
        layout.addComponent(reasonSelect);

        final TextField reasonCommentTextField = new TextField("Reason comment");
        reasonCommentTextField.setWidth(WIDTH);
        if (te.getReasonComment() != null) {
            reasonCommentTextField.setValue(te.getReasonComment());
        }
        layout.addComponent(reasonCommentTextField);

        Button saveButton = new Button("Save");
        saveButton.addClickListener(new Button.ClickListener() {

            @Override
            @Transactional
            public void buttonClick(Button.ClickEvent event) {
                try {
                    if (!commentTextField.getValue().equals(te.getComment())) {
                        daoContainer.getTestExecutionDao().setComment(te.getId(), commentTextField.getValue());
                        Notification.show("Comment updated", Notification.Type.TRAY_NOTIFICATION);
                    }
                    if (!reasonSelect.getValue().equals(te.getFailReason())) {
                        daoContainer.getTestExecutionDao().setFailedReason(te.getId(), (String) reasonSelect.getValue());
                        Notification.show("Reason updated", Notification.Type.TRAY_NOTIFICATION);
                    }
                    if (!reasonCommentTextField.getValue().equals(te.getReasonComment())) {
                        daoContainer.getTestExecutionDao().setReasonComment(te.getId(), reasonCommentTextField.getValue());
                        Notification.show("Reason comment updated", Notification.Type.TRAY_NOTIFICATION);
                    }
                    editWindow.close();
                    update();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        layout.addComponent(saveButton);


        editWindow.center();
        editWindow.setContent(layout);
        UI.getCurrent().addWindow(editWindow);

    }

    private void export() {
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
            } else if (te.getStatus() == PROCESSING.getIntegerValue()) {
                status = "processing";
            }
            text.append(te.getIssue() + ": " + te.getName() + " (" + te.getComment() + ") - " + status + "\n");
        }
        textLabel.setValue(text.toString());
        UI.getCurrent().addWindow(exportWindow);
    }
}
