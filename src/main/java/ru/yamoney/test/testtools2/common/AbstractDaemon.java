package ru.yamoney.test.testtools2.common;

import org.apache.log4j.Logger;

/**
 * Created by def on 23.11.14.
 */
public abstract class AbstractDaemon implements Runnable {
    public static final Logger LOG = Logger.getLogger(AbstractDaemon.class);

    private Integer period;
    private Boolean run;

    public AbstractDaemon(Integer period) {
        this.period = period;
        run = true;
    }

    public void start() {
        Thread thread = new Thread(this);
        thread.start();
    }

    public void stop() {
        run = false;
        LOG.info("Stopping daemon");
    }

    protected abstract void process();

    public void run() {
        while (run) {
            try {
                process();
                Thread.sleep(period * 1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
