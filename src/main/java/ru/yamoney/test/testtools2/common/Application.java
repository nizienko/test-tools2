package ru.yamoney.test.testtools2.common;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by def on 31.03.15.
 */
public class Application {
    public static final Logger LOG = Logger.getLogger(Application.class);
    private static List<ApplicationThread> apps;
    private static boolean embeddedServerMode = false;
    private static CloseableHttpClient httpClient;

    private static ApplicationContext ctx = null;

    public static void main(String[] args) {
        embeddedServerMode = true;
        getCtx();
    }

    public static void addThread(ApplicationThread at) {
        LOG.info(at + " going to start");
        apps.add(at);
        at.start();
    }

    public static void stopThread(ApplicationThread at){
        at.stop();
        apps.remove(at);
    }

    public static void stopAllThreads(){
        for (ApplicationThread at : apps){
            at.stop();
            LOG.info(at + " will stop");
        }
        apps.clear();
    }

    public synchronized static ApplicationContext getCtx() {
        if (ctx == null) {
            createHttpClient();
            apps = new ArrayList<>();
            ctx = new ClassPathXmlApplicationContext("beans.xml");
        }
        return ctx;
    }

    public static CloseableHttpClient getHttpClient(){
        return httpClient;
    }

    public static boolean isEmbeddedServerMode() {
        return embeddedServerMode;
    }

    private static void createHttpClient(){
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setSocketTimeout(3000)
                .setConnectTimeout(3000)
                .setConnectionRequestTimeout(3000)
                .setStaleConnectionCheckEnabled(true)
                .build();
        httpClient = HttpClients.custom()
                .setDefaultRequestConfig(defaultRequestConfig)
                .build();;
    }
}
