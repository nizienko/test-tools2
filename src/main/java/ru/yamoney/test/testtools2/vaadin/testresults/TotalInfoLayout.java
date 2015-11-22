package ru.yamoney.test.testtools2.vaadin.testresults;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import ru.yamoney.test.testtools2.common.Application;
import ru.yamoney.test.testtools2.common.DaoContainer;
import ru.yamoney.test.testtools2.testmanager.ExecutionStatus;

import java.util.*;

/**
 * Created by def on 19.04.15.
 */
public class TotalInfoLayout extends GridLayout {
    private Label label;
    private DaoContainer daoContainer;
    private Button updateButton;
    private TestResultsFilter filter;
    private Calendar calendar;

    public TotalInfoLayout() {
        super(1, 3);
        this.setRowExpandRatio(2, 1);
        this.setSizeFull();
        daoContainer = (DaoContainer) Application.getCtx().getBean("daoContainer");
        filter = new TestResultsFilter();
        calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, -8);
        filter.setSinceDate(calendar.getTime());
        filter.setToDate(new Date());
        updateButton = new Button("Load");
        updateButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                updatePage();
            }
        });

        label = new Label();
        label.setContentMode(ContentMode.HTML);
        this.addComponent(updateButton);
        this.addComponent(label);
//        updatePage();
    }

    private void updatePage() {
        StringBuffer text = new StringBuffer();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR, -8);
        filter.setSinceDate(calendar.getTime());
        filter.setToDate(new Date());
        for (Map<String, Object> p : daoContainer.getTestExecutionDao().selectProjects(filter)) {
            String project = (String) p.get("project");
            text.append("<h2>" + project + "</h2>");
            filter.setProject(project);
            for (Map<String, Object> v : daoContainer.getTestExecutionDao().selectVersions(filter)) {
                String version = (String) v.get("version");
                text.append("<h3>" + version + "</h3>");
                filter.setVersion(version);
                for (Map<String, Object> b : daoContainer.getTestExecutionDao().selectBuilds(filter)) {
                    String build = (String) b.get("build");
                    filter.setBuild(build);
                    Set<String> issues = new HashSet<>();
                    for (Map<String, Object> issue : daoContainer.getTestExecutionDao().selectIssues(filter)) {
                        issues.add((String) issue.get("issue"));
                    }
                    text.append("<table><tr><td>" + version + "." + build + "</td><td>Always failed tests:</td></tr><tr><td>");
                    text.append("Test cases: " + issues.size() + "<br>");
                    int passed = 0;
                    int passedSomteimes = 0;
                    int failed = 0;
                    List<String> failedTests = new ArrayList<>();
                    for (String issue : issues) {
                        filter.setIssue(issue);

                        filter.setStatus(ExecutionStatus.PASSED.getValue());
                        int passedTimes = daoContainer.getTestExecutionDao().countByFilter(filter);
                        filter.setStatus(ExecutionStatus.FAILED.getValue());
                        int failedTimes = daoContainer.getTestExecutionDao().countByFilter(filter);
                        filter.setStatus(null);
                        if (passedTimes > 0 && failedTimes == 0) {
                            passed++;
                        } else if (passedTimes > 0 && failedTimes > 0) {
                            passedSomteimes++;
                        } else if (passedTimes == 0 && failedTimes > 0) {
                            failed++;
                            failedTests.add(issue + ": " + daoContainer.getTestExecutionDao().getNameByIssue(issue) + "(" + failedTimes + ")");
                        }
                    }
                    filter.setIssue(null);
                    filter.setBuild(null);
                    text.append("Always passed: " + passed + "<br>");
                    if (passedSomteimes > 0) {
                        text.append("Sometimes passed: " + passedSomteimes + "<br>");
                    }
                    text.append("Always failed: " + failed + "<br>");
                    text.append("</td><td>");
                    for (String failedTest : failedTests) {
                        text.append("<pre>" + failedTest + "<br>");
                    }
                    text.append("</td></tr></table><br>");

                }
                filter.setVersion(null);
            }
            filter.setProject(null);
            text.append("<hr>");
        }
        label.setValue(text.toString());
    }

}
