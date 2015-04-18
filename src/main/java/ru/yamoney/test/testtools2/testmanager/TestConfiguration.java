package ru.yamoney.test.testtools2.testmanager;

/**
 * Created by nizienko on 12.11.14.
 */
public class TestConfiguration {
    private Integer id;
    private String name;
    private String description;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return this.getName();
    }
}
