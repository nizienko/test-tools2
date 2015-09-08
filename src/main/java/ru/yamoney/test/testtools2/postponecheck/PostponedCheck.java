package ru.yamoney.test.testtools2.postponecheck;

import org.json.JSONObject;
import ru.yamoney.test.testtools2.testmanager.ExecutionStatus;

/**
 * Created by def on 08.09.15.
 */
public interface PostponedCheck {
    JSONObject getJSON();
    void process();
    int getStatus();
    String getDescription();
}
