package ru.yamoney.test.testtools2.vaadin.testresults;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by def on 16.04.15.
 */
public class TestResultsFilter {
    private String project;
    private String version;
    private String build;
    private String execution;
    private String issue;
    private String issueTemp;
    private String status;
    private Date sinceDate;
    private Date toDate;
    private List<Object> objects;


    public TestResultsFilter() {
        project = null;
        version = null;
        build = null;
        execution = null;
        issue = null;
        issueTemp = null;
        status = null;
        objects = new ArrayList<>();
    }



    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getBuild() {
        return build;
    }

    public void setBuild(String build) {
        this.build = build;
    }

    public String getExecution() {
        return execution;
    }

    public void setExecution(String execution) {
        this.execution = execution;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public void setIssueTemp(String issue){
        this.issueTemp = issue;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getSinceDate() {
        return sinceDate;
    }

    public void setSinceDate(Date sinceDate) {
        this.sinceDate = sinceDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public String getSql() {
        objects.clear();
        StringBuffer sql = new StringBuffer();
        sql.append(" where execution_dt between ? and ?");
        objects.add(sinceDate);
        objects.add(toDate);
        if (project != null && !"".equals(project)) {
            sql.append(" and data->>'project'=?");
            objects.add(project);
        }
        if (version != null && !"".equals(version)) {
            sql.append(" and data->>'version'=?");
            objects.add(version);
        }
        if (build != null && !"".equals(build)) {
            sql.append(" and data->>'build'=?");
            objects.add(build);
        }
        if (execution != null && !"".equals(execution)) {
            sql.append(" and data->>'execution'=?");
            objects.add(execution);
        }
        if (issue != null && !"".equals(issue)) {
            sql.append(" and data->>'issue'=?");
            objects.add(issue);
        }
        else if (issueTemp != null && !"".equals(issueTemp)){
            sql.append(" and data->>'issue'=?");
            objects.add(issueTemp);
        }
        if (status != null && !"".equals(status)) {
            sql.append(" and data->>'status'=?");
            objects.add(status);
        }
        return sql.toString();
    }

    public Object[] getObjects(){
        return objects.toArray();
    }
}
