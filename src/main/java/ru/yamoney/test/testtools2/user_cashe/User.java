package ru.yamoney.test.testtools2.user_cashe;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by def on 22.11.15.
 */
public class User {
    private String userName;
    private String account;
    private String host;
    private String phone;
    private String password;
    private UserStatus status;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getJsonString(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userName", userName);
            jsonObject.put("password", password);
            jsonObject.put("account", account);
            jsonObject.put("host", host);
            jsonObject.put("phone", phone);
            jsonObject.put("status", status.getValue() + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}
