package ru.yamoney.test.testtools2.vaadin.informer_ui;

import com.vaadin.event.UIEvents;
import com.vaadin.server.Sizeable;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.UI;


/**
 * Created by def on 30.11.15.
 */
public class InformerUI extends UI {
    private InformerTestStandLayout informerTestStandLayout;
    private InformerLastTestsLayout informerLastTestsLayout;

    @Override
    protected void init(VaadinRequest request) {
        informerTestStandLayout = new InformerTestStandLayout();
        informerLastTestsLayout = new InformerLastTestsLayout();
        HorizontalSplitPanel horizontalSplitPanel = new HorizontalSplitPanel();
        horizontalSplitPanel.setFirstComponent(informerTestStandLayout);
        horizontalSplitPanel.setSecondComponent(informerLastTestsLayout);
        horizontalSplitPanel.setSplitPosition(40, Unit.PERCENTAGE);

        setContent(horizontalSplitPanel);
        setPollInterval(5000);
        this.addPollListener(new UIEvents.PollListener() {
            @Override
            public void poll(UIEvents.PollEvent pollEvent) {
                update();
            }
        });
        setTheme("mytheme");
    }


    private void update(){
        informerTestStandLayout.update();
        informerLastTestsLayout.update();
    }
}
