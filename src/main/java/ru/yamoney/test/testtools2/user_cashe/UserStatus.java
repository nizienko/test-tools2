package ru.yamoney.test.testtools2.user_cashe;

/**
 * Created by def on 22.11.15.
 */
public enum UserStatus {
    NEW(0), USED(2), BUSY(1);

    private int i;

    UserStatus(int i){
        this.i = i;
    }

    public int getValue(){
        return i;
    }
}
