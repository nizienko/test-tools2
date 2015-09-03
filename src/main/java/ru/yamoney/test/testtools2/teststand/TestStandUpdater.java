package ru.yamoney.test.testtools2.teststand;

/**
 * Created by def on 04.09.15.
 */
public class TestStandUpdater implements Runnable {
    private TestStand testStand;

    public TestStandUpdater(TestStand testStand) {
        this.testStand = testStand;
    }

    @Override
    public void run() {
        testStand.checkResources();
    }
}
