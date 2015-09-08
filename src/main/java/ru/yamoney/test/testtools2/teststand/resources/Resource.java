package ru.yamoney.test.testtools2.teststand.resources;

import org.json.JSONObject;

/**
 * Created by def on 27.05.15.
 */
public interface Resource {

    public void init(JSONObject data);

    public ResourceStatus getStatus();

    public void checkStatus();

}
