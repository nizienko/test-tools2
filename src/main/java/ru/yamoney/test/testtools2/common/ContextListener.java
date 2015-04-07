package ru.yamoney.test.testtools2.common;

import org.apache.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by nizienko on 05.12.14.
 */
public class ContextListener implements ServletContextListener {
    public static final Logger LOG = Logger.getLogger(ContextListener.class);

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        LOG.info("Started");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        LOG.info("Stopping");
        Application.stopAllThreads();
    }
}
