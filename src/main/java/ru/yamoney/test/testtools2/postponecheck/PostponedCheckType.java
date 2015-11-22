package ru.yamoney.test.testtools2.postponecheck;

/**
 * User: nizienko
 * Date: 08.09.2015
 * Time: 13:59
 */
public enum PostponedCheckType {

    SELECT(1);

    private int i;

    PostponedCheckType(int i) {
        this.i = i;
    }

    int getValue() {
        return i;
    }
}
