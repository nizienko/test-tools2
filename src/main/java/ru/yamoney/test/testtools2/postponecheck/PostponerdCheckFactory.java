package ru.yamoney.test.testtools2.postponecheck;

import com.sun.istack.internal.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * User: nizienko
 * Date: 08.09.2015
 * Time: 14:02
 */
public class PostponerdCheckFactory {
    @Nullable
    public static PostponedCheck getPostponedCheck(JSONObject jsonObject) {
        try {
            if (jsonObject.getInt("type") == PostponedCheckType.SELECT.getValue()) {
                return new PostponedSelectCheck(jsonObject);
            }
        } catch (JSONException e) {
            return null;
        }
        return null;
    }
}
