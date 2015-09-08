package ru.yamoney.test.testtools2.testmanager;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ru.yamoney.test.testtools2.postponecheck.PostponedCheck;
import ru.yamoney.test.testtools2.postponecheck.PostponerdCheckFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private Integer published;
    private String failReason;
    private String reasonComment;
    private Integer acceptance;
    private List<PostponedCheck> postponedCheckList;
    public static final Logger LOG = Logger.getLogger(TestExecution.class);

    public String toString(){
        return name;
    }

    public JSONObject getJSON(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("project", project);
            jsonObject.put("version", version);
            jsonObject.put("build", build);
            jsonObject.put("execution", execution);
            jsonObject.put("issue",  issue);
            jsonObject.put("name",  name);
            jsonObject.put("status", String.valueOf(status));
            jsonObject.put("comment", comment);
            jsonObject.put("published", String.valueOf(published));
            if (failReason != null) {
                jsonObject.put("reason", failReason);
            }
            if (reasonComment != null) {
                jsonObject.put("reasonComment", reasonComment);
            }
            if (acceptance != null) {
                jsonObject.put("acceptance", String.valueOf(acceptance));
            }
            if (postponedCheckList != null) {
                JSONArray jsonArray = new JSONArray();
                for (PostponedCheck postponedCheck : postponedCheckList) {
                    jsonArray.put(postponedCheck.getJSON());
                }
                jsonObject.put("postponedCheckList", jsonArray);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
            published = Integer.parseInt((String) jsonObject.get("published"));
            try {
                failReason = (String) jsonObject.get("reason");
            }
            catch (Exception e) {
                failReason = null;
            }
            try {
                reasonComment = (String) jsonObject.get("reasonComment");
            }
            catch (Exception e) {
                reasonComment = null;
            }
            try {
                acceptance = Integer.parseInt((String) jsonObject.get("acceptance"));
            }
            catch (Exception e) {
                acceptance = null;
            }
            try {
                JSONArray jsonArray = (JSONArray) jsonObject.get("postponedCheckList");
                postponedCheckList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    postponedCheckList.add(PostponerdCheckFactory.getPostponedCheck((JSONObject) jsonArray.get(i)));
                }
            }
            catch (Exception e) {
                postponedCheckList = null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setPostponedCheckList(JSONArray jsonArray){
        postponedCheckList = new ArrayList<>();
        LOG.info("array:" + jsonArray.toString());
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                postponedCheckList.add(
                        PostponerdCheckFactory
                                .getPostponedCheck(
                                        (JSONObject) jsonArray
                                                .get(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
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
        return published;
    }

    public void setPublished(Integer isPublicated) {
        this.published = isPublicated;
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

    public String getReasonComment() {
        return reasonComment;
    }

    public void setReasonComment(String reasonComment) {
        this.reasonComment = reasonComment;
    }

    public Integer getAcceptance() {
        return acceptance;
    }

    public void setAcceptance(Integer acceptance) {
        this.acceptance = acceptance;
    }

    public List<PostponedCheck> getPostponedCheckList() {
        return postponedCheckList;
    }

    public void setPostponedCheckList(List<PostponedCheck> postponedCheckList) {
        this.postponedCheckList = postponedCheckList;
    }
}
