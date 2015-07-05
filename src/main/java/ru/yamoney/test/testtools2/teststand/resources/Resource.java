package ru.yamoney.test.testtools2.teststand.resources;

/**
 * Created by def on 27.05.15.
 */
public interface Resource {

    public void init(String data);

    public ResourceStatus getStatus();

}
