package ru.yamoney.test.testtools2.teststand.resources;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import ru.yamoney.test.testtools2.common.Application;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by nizienko on 18.08.15.
 */
public class PingHttpResource implements Resource {
    public static final Logger LOG = Logger.getLogger(PingHttpResource.class);
    private String url;
    private ResourceStatus resourceStatus;
    private JSONObject dataJSON;

    @Override
    public String toString(){
        return resourceStatus.getName();
    }

    @Override
    public void init(JSONObject data) {
        resourceStatus = new ResourceStatus(5);
        dataJSON = data;
        if (dataJSON == null) {
            resourceStatus.setBroken(true);
        }
        try {
            url = (String) dataJSON.get("url");
        } catch (JSONException e) {
            resourceStatus.setBroken(true);
            LOG.error(e.getMessage());
        }
        try {
            String name = (String) dataJSON.get("name");
            resourceStatus.setName(name);
        } catch (JSONException e) {
            LOG.error(e.getMessage());
            resourceStatus.setName(url);
        }
    }

    @Override
    public ResourceStatus getStatus() {
        return resourceStatus;
    }

    public void checkStatus() {
        if (resourceStatus.isDataToOld()) {
            check();
        }
    }

    private synchronized void check() {
        LOG.info("Checking....");
        resourceStatus.updateLastCheck();
        String version = null;
        String status = null;
        String statusMessage = null;
        int responseStatus = -1;
        HttpGet httpget = new HttpGet(url);
        try {
            String contentType = (String) dataJSON.get("ContentType");
            if (contentType != null){
                httpget.setHeader("Content-Type", contentType);
            }
            else {
                httpget.setHeader("Content-Type", "text/html; charset=UTF-8");
            }
        }
        catch (Exception e) {
            httpget.setHeader("Content-Type", "text/html; charset=UTF-8");
        }
        try {
            JSONObject root;
            HttpResponse response = Application.getHttpClient().execute(httpget);
            try {
                BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                StringBuffer responseBody = new StringBuffer();
                String line;
                while ((line = rd.readLine()) != null) {
                    responseBody.append(line + "\r\n");
                }
                root = new JSONObject(responseBody.toString());
                responseStatus = response.getStatusLine().getStatusCode();
            }
            finally {
                EntityUtils.consumeQuietly(response.getEntity());
            }
            JSONObject serviceInfoJson = (JSONObject) root.get("serviceInfo");
            version = (String) serviceInfoJson.get("version");
            JSONObject statusJson = (JSONObject) root.get("status");
            status = statusJson.getString("code");
            statusMessage = statusJson.getString("message");
        }

        catch (Exception e) {
            LOG.error("ERROR: " + e.getMessage());
            resourceStatus.setStatus(false, e.getMessage());
        } finally {
            httpget.releaseConnection();
        }
        if (responseStatus == 200) {
            resourceStatus.setStatus(true, "Version: " + version + ";    Status: " + status + "(" + statusMessage + ")");
        }
    }
}
