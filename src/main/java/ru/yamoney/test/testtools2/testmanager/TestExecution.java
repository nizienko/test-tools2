package ru.yamoney.test.testtools2.testmanager;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

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
    private Date executionDt;
    private Date lastChangeDt;
    private Integer status;
    private String comment;
    private Integer publicated;
    private String failReason;
    public static final Logger LOG = Logger.getLogger(TestExecution.class);


    public JSONObject getJSON(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("project", project);
            jsonObject.put("version", version);
            jsonObject.put("build", build);
            jsonObject.put("execution", execution);
            jsonObject.put("issue",  issue);
            jsonObject.put("name",  name);
            jsonObject.put("status", status + "");
            jsonObject.put("comment", comment);
            jsonObject.put("publicated", publicated + "");
            if (failReason != null) {
                jsonObject.put("reason", failReason);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LOG.info(jsonObject.toString());
        return jsonObject;
    }

    public String getData(){
        return getJSON().toString();
    }

    public void setData(String data){
        try {
            JSONObject jsonObject = new JSONObject(data);
            project = (String) jsonObject.get("project");
            version = (String) jsonObject.get("version");
            build = (String) jsonObject.get("build");
            execution = (String) jsonObject.get("execution");
            issue = (String) jsonObject.get("issue");
            name = (String) jsonObject.get("name");
            status = Integer.parseInt((String) jsonObject.get("status"));
            comment = (String) jsonObject.get("comment");
            publicated = Integer.parseInt((String) jsonObject.get("publicated"));
            try {
                failReason = (String) jsonObject.get("reason");
            }
            catch (Exception e) {
                failReason = null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

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

    public Integer isPublicated() {
        return publicated;
    }

    public void setPublicated(Integer isPublicated) {
        this.publicated = isPublicated;
        this.setLastChangeDt(new Date());
    }

    public Date getExecutionDt() {
        return executionDt;
    }

    public void setExecutionDt(Date executionDt) {
        this.executionDt = executionDt;
    }

    public Date getLastChangeDt() {
        return lastChangeDt;
    }

    public void setLastChangeDt(Date lastChangeDt) {
        this.lastChangeDt = lastChangeDt;
    }

    public String getFailReason() {
        return failReason;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason;
    }
}
