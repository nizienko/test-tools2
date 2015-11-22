package ru.yamoney.test.testtools2.postponecheck;

import org.apache.log4j.Logger;
import ru.yamoney.test.testtools2.common.AbstractDaemon;
import ru.yamoney.test.testtools2.common.Application;
import ru.yamoney.test.testtools2.common.ApplicationThread;
import ru.yamoney.test.testtools2.common.DaoContainer;

/**
 * Created by def on 07.09.15.
 */
public class PostponedCheckDaemon extends AbstractDaemon implements ApplicationThread {
    public static final Logger LOG = Logger.getLogger(PostponedCheckDaemon.class);
    private PostponedChecker postponedChecker;
    private DaoContainer daoContainer;
    public PostponedCheckDaemon(Integer period) {
        super(period);
    }

    public void setDaoContainer(DaoContainer daoContainer) {
        this.daoContainer = daoContainer;
    }

    public void init() {
        Application.addThread(this);
        postponedChecker = new PostponedChecker(daoContainer);
    }

    @Override
    protected void process() {
        postponedChecker.work();
    }

    @Override
    public String toString() {
        return "PostponedCheckDaemon";
    }
}
