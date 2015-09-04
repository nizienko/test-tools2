package ru.yamoney.test.testtools2.teststand;

import ru.yamoney.test.testtools2.common.AbstractDaemon;
import ru.yamoney.test.testtools2.common.ApplicationThread;

/**
 * Created by def on 04.09.15.
 */
public class TestStandUpdater extends AbstractDaemon implements ApplicationThread {
    private TestStand testStand;

    public TestStandUpdater(Integer period) {
        super(period);
    }


    public void setTestStand(TestStand testStand) {
        this.testStand = testStand;
    }

    @Override
    protected void process() {
        testStand.checkResources();
    }
}
