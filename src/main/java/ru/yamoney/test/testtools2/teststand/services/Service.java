package ru.yamoney.test.testtools2.teststand.services;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import ru.yamoney.test.testtools2.common.Application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by def on 05.07.15.
 */
public class Service {
    public static final Logger LOG = Logger.getLogger(Service.class);

    private List<NameValuePair> hiddenParams;
    private List<NameValuePair> editableParams;
    private String action;
    private String name;

    public Service(String data){
        hiddenParams = new ArrayList<NameValuePair>();
        editableParams = new ArrayList<NameValuePair>();
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(data);
            action = jsonObject.getString("action");
            name = jsonObject.getString("name");
            JSONObject hiddenJSONParams = jsonObject.getJSONObject("hiddenParams");
            if (hiddenJSONParams != null) {
                Iterator<String> keys = hiddenJSONParams.keys();
                while(keys.hasNext()){
                    String key = keys.next();
                    String val = null;
                    val = hiddenJSONParams.getString(key);
                    hiddenParams.add(new BasicNameValuePair(key, val));
                }
            }

            JSONObject editableJSONParams = jsonObject.getJSONObject("editableParams");
            if (editableJSONParams != null) {
                Iterator<String> keys = editableJSONParams.keys();
                while(keys.hasNext()){
                    String key = keys.next();
                    String val = null;
                    val = editableJSONParams.getString(key);
                    editableParams.add(new BasicNameValuePair(key, val));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public List<NameValuePair> getAllParams(){
        List<NameValuePair> all = new ArrayList<>();
        all.addAll(editableParams);
        all.addAll(hiddenParams);
        LOG.info("request params: " + all);
        return all;
    }

    public List<NameValuePair> getEditableParams(){
        return editableParams;
    }

    public void setEditableParams(List<NameValuePair> editableParams) {
        this.editableParams = editableParams;
    }

    public String sendRequest(){
        HttpPost httpPost = new HttpPost(action);
        LOG.info("Request to " + action);
        httpPost.setHeader("Content-Type", "text/html; charset=UTF-8");
        httpPost.setEntity(new UrlEncodedFormEntity(getAllParams(), Charset.forName("UTF-8")));

        String responseString = "";
        HttpResponse response = null;
        try {
            response = Application.getHttpClient().execute(httpPost);
            LOG.info(response.getStatusLine());
            try {
                BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                StringBuffer responseBody = new StringBuffer();
                String line;
                while ((line = rd.readLine()) != null) {
                    LOG.info(line);
                    responseBody.append(line);
                }
                responseString = responseBody.toString();
            }
            catch (Exception e) {
                return  e.getMessage();
            }
            finally {
                EntityUtils.consumeQuietly(response.getEntity());
            }
        } catch (IOException e) {
            return e.getMessage();
        }
        LOG.info(responseString);
        return responseString;
    }

    public String getName() {
        return name;
    }

}
