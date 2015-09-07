package ru.yamoney.test.testtools2.postponecheck;

import org.apache.log4j.Logger;
import ru.yamoney.test.testtools2.common.AbstractDaemon;
import ru.yamoney.test.testtools2.common.Application;
import ru.yamoney.test.testtools2.common.ApplicationThread;

/**
 * Created by def on 07.09.15.
 */
public class PostponedCheckDaemon extends AbstractDaemon implements ApplicationThread {
    public static final Logger LOG = Logger.getLogger(PostponedCheckDaemon.class);

    public PostponedCheckDaemon(Integer period) {
        super(period);
    }

    public void init() {
        Application.addThread(this);
    }

    @Override
    protected void process() {
        LOG.info("Process postponed checks");
    }
}
