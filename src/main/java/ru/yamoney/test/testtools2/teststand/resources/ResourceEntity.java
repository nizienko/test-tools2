package ru.yamoney.test.testtools2.teststand.resources;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by def on 28.05.15.
 */
public class ResourceEntity {
    private int id;
    private String type;
    private JSONObject data;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public JSONObject getData() {
        return data;
    }

    public void setData(String data) {
        try {
            this.data = new JSONObject(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
