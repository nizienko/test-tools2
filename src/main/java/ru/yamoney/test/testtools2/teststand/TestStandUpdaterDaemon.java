package ru.yamoney.test.testtools2.teststand;

import ru.yamoney.test.testtools2.common.AbstractDaemon;
import ru.yamoney.test.testtools2.common.Application;
import ru.yamoney.test.testtools2.common.ApplicationThread;

/**
 * Created by def on 04.09.15.
 */
public class TestStandUpdaterDaemon extends AbstractDaemon implements ApplicationThread {
    private TestStandUpdater testStandUpdater;

    public TestStandUpdaterDaemon(Integer period) {
        super(period);
    }

    public void init() {
        Application.addThread(this);
    }

    @Override
    protected void process() {
        if (testStandUpdater == null) {
            testStandUpdater = new TestStandUpdater((TestStand) Application.getCtx().getBean("testStand"));
        }
        for (int i = 0; i < 5; i++) {
            Thread thread = new Thread(testStandUpdater);
            thread.start();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
