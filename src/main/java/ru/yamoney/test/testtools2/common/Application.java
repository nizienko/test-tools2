package ru.yamoney.test.testtools2.common;

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

    private static ApplicationContext ctx = null;
    public static void main(String[] args) {
        apps = new ArrayList<>();
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
            ctx = new ClassPathXmlApplicationContext("beans.xml");
        }
        return ctx;
    }
}
