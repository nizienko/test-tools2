package ru.yamoney.test.testtools2.testmanager;

/**
 * Created by def on 02.04.15.
 */
public class TestExecution {
    private int id;
    private String project;
    private String version;
    private String build;
    private String execution;
    private String issue;
    private String name;
    private Integer status;
    private String comment;
    private int isPublicated;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int isPublicated() {
        return isPublicated;
    }

    public void setPublicated(int isPublicated) {
        this.isPublicated = isPublicated;
    }
}
