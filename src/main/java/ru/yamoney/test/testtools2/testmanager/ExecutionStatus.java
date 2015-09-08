package ru.yamoney.test.testtools2.testmanager;

/**
 * Created by def on 18.04.15.
 */
public enum ExecutionStatus {
    PASSED(5), FAILED(6), PROCESSING(10);

    private Integer value;
    ExecutionStatus(Integer status) {
        this.value = status;
    }

    public String getValue(){
        return String.valueOf(value);
    }

    public Integer getIntegerValue(){
        return value;
    }

}
