package ru.yamoney.test.testtools2.vaadin.informer_ui;

import com.vaadin.event.Action;
import com.vaadin.event.UIEvents;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;


/**
 * Created by def on 30.11.15.
 */
public class InformerUI extends UI {
    private InformerTestStandLayout informerTestStandLayout;
    private boolean running;
    @Override
    protected void init(VaadinRequest request) {
        running = true;
        informerTestStandLayout = new InformerTestStandLayout();
        setContent(informerTestStandLayout);
        setPollInterval(1000);
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
    }
}
