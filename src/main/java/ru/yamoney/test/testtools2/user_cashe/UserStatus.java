package ru.yamoney.test.testtools2.user_cashe;

/**
 * Created by def on 22.11.15.
 */
public enum UserStatus {
    NEW(0, "name"), USED(2, "old"), BUSY(1, "busy");

    private int i;
    private String restName;

    UserStatus(int i, String restName){
        this.i = i; this.restName = restName;
    }

    public int getValue(){
        return i;
    }

    public String getRestName() {
        return restName;
    }
}
