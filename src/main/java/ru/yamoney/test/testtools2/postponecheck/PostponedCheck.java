package ru.yamoney.test.testtools2.postponecheck;

import org.json.JSONObject;

/**
 * Created by def on 08.09.15.
 */
public interface PostponedCheck {
    JSONObject getJSON();

    void check();

}
