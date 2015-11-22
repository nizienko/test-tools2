package ru.yamoney.test.testtools2.vaadin.testresults;

import com.vaadin.data.Item;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Table;
import ru.yamoney.test.testtools2.common.Application;
import ru.yamoney.test.testtools2.common.DaoContainer;
import ru.yamoney.test.testtools2.testmanager.ExecutionStatus;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by def on 18.04.15.
 */
public class CompareExecutionsLayout extends GridLayout {
    private Table table;
    private TestResultsFilterLayout testResultsFilterLayout1;
    private TestResultsFilterLayout testResultsFilterLayout2;
    private Button updateButton;
    private DaoContainer daoContainer;

    public CompareExecutionsLayout() {
        super(1, 4);
        this.setRowExpandRatio(3, 1);
        this.setSizeFull();
        daoContainer = (DaoContainer) Application.getCtx().getBean("daoContainer");

        testResultsFilterLayout1 = new TestResultsFilterLayout(daoContainer);
        testResultsFilterLayout2 = new TestResultsFilterLayout(daoContainer);

        this.addComponent(testResultsFilterLayout1);
        this.addComponent(testResultsFilterLayout2);
        updateButton = new Button("Compare");
        updateButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                update();
            }
        });
        this.addComponent(updateButton);
        table = new Table();
        table.setWidth("100%");
        table.setHeight("100%");
        table.setEditable(false);
        table.setSelectable(true);
        table.setImmediate(true);
        table.addContainerProperty("", Integer.class, null);
        table.addContainerProperty("Issue", String.class, null);
        table.addContainerProperty("Name", String.class, null);
        table.addContainerProperty("Old status", String.class, null);
        table.addContainerProperty("New status", String.class, null);
        table.addContainerProperty("Total", String.class, null);
        table.setCellStyleGenerator(new Table.CellStyleGenerator() {
            @Override
            public String getStyle(Table source, Object itemId, Object propertyId) {
                if (propertyId != null) {
                    Item item = source.getItem(itemId);
                    String status = (String) item.getItemProperty("Total").getValue();
                    if ("+".equals(status)) {
                        return "passed";
                    } else if ("-".equals(status)) {
                        return "failed";
                    } else {
                        return "not_run";
                    }
                } else {
                    return null;
                }
            }
        });
        this.addComponent(table);
    }

    private void update() {
        table.removeAllItems();
        Set<String> issues = new HashSet<>();
        for (Map<String, Object> p : daoContainer.getTestExecutionDao().selectIssues(testResultsFilterLayout1.getFilter())) {
            issues.add((String) p.get("issue"));
        }
        for (Map<String, Object> p : daoContainer.getTestExecutionDao().selectIssues(testResultsFilterLayout2.getFilter())) {
            issues.add((String) p.get("issue"));
        }
        int i = 1;
        for (String issue : issues) {
            String name = daoContainer.getTestExecutionDao().getNameByIssue(issue);
            testResultsFilterLayout1.getFilter().setIssueTemp(issue);
            testResultsFilterLayout1.getFilter().setStatus(ExecutionStatus.FAILED.getValue());
            Integer failed1 = daoContainer.getTestExecutionDao().countByFilter(testResultsFilterLayout1.getFilter());
            testResultsFilterLayout1.getFilter().setStatus(ExecutionStatus.PASSED.getValue());
            Integer passed1 = daoContainer.getTestExecutionDao().countByFilter(testResultsFilterLayout1.getFilter());
            testResultsFilterLayout1.getFilter().setStatus(null);
            testResultsFilterLayout1.getFilter().setIssueTemp(null);

            Integer status1 = getStatus(failed1, passed1);

            testResultsFilterLayout2.getFilter().setIssueTemp(issue);
            testResultsFilterLayout2.getFilter().setStatus(ExecutionStatus.FAILED.getValue());
            Integer failed2 = daoContainer.getTestExecutionDao().countByFilter(testResultsFilterLayout2.getFilter());
            testResultsFilterLayout2.getFilter().setStatus(ExecutionStatus.PASSED.getValue());
            Integer passed2 = daoContainer.getTestExecutionDao().countByFilter(testResultsFilterLayout2.getFilter());
            testResultsFilterLayout2.getFilter().setStatus(null);
            testResultsFilterLayout2.getFilter().setIssueTemp(null);

            Integer status2 = getStatus(failed2, passed2);

            table.addItem(new Object[]{
                    i,
                    issue,
                    name,
                    getStatusText(status1) + "(" + passed1 + "/" + (passed1 + failed1) + ")",
                    getStatusText(status2) + "(" + passed2 + "/" + (passed2 + failed2) + ")",
                    getTotalStatus(status1, status2)
            }, new Integer(i));
            i++;
        }


    }

    private Integer getStatus(Integer failed, Integer passed) {
        Integer status = 4;
        if (failed == 0 && passed > 0) {
            status = 1;
        } else if (failed > 0 && passed > 0) {
            status = 2;
        } else if (failed > 0 && passed == 0) {
            status = 3;
        }
        return status;
    }

    private String getStatusText(Integer status) {
        switch (status) {
            case 1:
                return "passed";
            case 2:
                return "passed sometimes";
            case 3:
                return "failed";
            case 4:
                return "not run";
        }
        return "unknown";
    }

    private String getTotalStatus(Integer status1, Integer status2) {
        if (status1 > 1 && status2 == 1) {
            return "+";
        }
        if (status1 == 1 && status2 > 1) {
            return "-";
        }
        return "";
    }
}
