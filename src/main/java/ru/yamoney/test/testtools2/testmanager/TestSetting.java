package ru.yamoney.test.testtools2.testmanager;

/**
 * Created by nizienko on 12.11.14.
 */
public class TestSetting {
    private String parameterName;
    private String description;
    private String value;

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.getParameterName();
    }
}
