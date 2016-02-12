package ru.yamoney.test.testtools2.teststand.resources;

import java.util.Date;

/**
 * Created by def on 28.05.15.
 */
public class ResourceStatus {
    private String name;
    private String statusMeassage;
    private boolean isBroken;
    private boolean isOnline;
    private Date lastCheck;
    private int s; // Сколько секунд не обновлять данные

    public ResourceStatus(int s) {
        this.s = s;
        this.statusMeassage = "not checked yet";
        this.isBroken = false;
        this.isOnline = false;
    }

    public void updateLastCheck() {
        lastCheck = new Date();
    }

    public void setStatus(boolean isOnline, String statusMeassage) {
        this.isOnline = isOnline;
        this.statusMeassage = statusMeassage;
        lastCheck = new Date();
    }

    public boolean isDataToOld() {
        return (lastCheck == null || (new Date().getTime() - lastCheck.getTime() > s * 1000));
    }

    public String getStatusMeassage() {
        return statusMeassage;
    }

    public void setStatusMeassage(String statusMessage) {
        this.statusMeassage = statusMessage;
    }

    public boolean isBroken() {
        return isBroken;
    }

    public void setBroken(boolean isBroken) {
        this.isBroken = isBroken;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean isOnline) {
        this.isOnline = isOnline;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getLastCheck() {
        return lastCheck;
    }
}
