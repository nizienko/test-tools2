package ru.yamoney.test.testtools2.teststand;

import ru.yamoney.test.testtools2.common.Application;


/**
 * Created by def on 04.09.15.
 */
public class TestStandUpdaterDaemon {
    private TestStand testStand;
    private Integer count;

    public void setCount(Integer count) {
        this.count = count;
    }

    public void init() {
        for (int i = 0; i < count; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            TestStandUpdater testStandUpdater = new TestStandUpdater(5);
            testStandUpdater.setTestStand(testStand);
            Application.addThread(testStandUpdater);
        }
    }

    public void setTestStand(TestStand testStand) {
        this.testStand = testStand;
    }
}
