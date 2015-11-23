package ru.yamoney.test.testtools2.user_cashe.db;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.jdbc.core.RowMapper;
import ru.yamoney.test.testtools2.user_cashe.User;
import ru.yamoney.test.testtools2.user_cashe.UserStatus;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by def on 22.11.15.
 */
public class UserMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        try {
            JSONObject dataObject = new JSONObject(rs.getString("data"));
            user.setAccount(dataObject.getString("account"));
            user.setPassword(dataObject.getString("password"));
            user.setHost(dataObject.getString("host"));
            user.setPhone(dataObject.getString("phone"));
            user.setUserName(dataObject.getString("userName"));
            int status = dataObject.getInt("status");
            if (status == 0) {
                user.setStatus(UserStatus.NEW);
            }
            else if (status == 1) {
                user.setStatus(UserStatus.BUSY);
            }
            else if (status == 2) {
                user.setStatus(UserStatus.USED);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }
}