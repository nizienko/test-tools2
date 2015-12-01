package ru.yamoney.test.testtools2.vaadin.informer_ui;

import com.vaadin.ui.VerticalLayout;
import org.apache.log4j.Logger;
import ru.yamoney.test.testtools2.common.Application;
import ru.yamoney.test.testtools2.teststand.TestStand;
import ru.yamoney.test.testtools2.teststand.resources.Resource;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by def on 30.11.15.
 */
public class InformerTestStandLayout extends VerticalLayout {
    private TestStand testStand;
    public static final Logger LOG = Logger.getLogger(InformerTestStandLayout.class);
    private List<ResourceStatusLayout> resources;

    public InformerTestStandLayout(){
        super();
        this.setWidth("100%");
        this.setHeight(null);
        testStand = (TestStand) Application.getCtx().getBean("testStand");
        resources = new LinkedList<>();
    }

    public void update() {
        if (resources.size() != testStand.getResources().size()) {
            updateSlots();
        }
        else {
            int g = 0;
            for (ResourceStatusLayout resourceStatusLayout : resources) {
                resourceStatusLayout.update(testStand.getResources().get(g));
                g++;
            }
        }
    }

    private void updateSlots(){
        for (ResourceStatusLayout resourceStatusLayout : resources) {
            this.removeComponent(resourceStatusLayout);
        }
        for (Resource resource : testStand.getResources()) {
            ResourceStatusLayout resourceStatusLayout = new ResourceStatusLayout();
            resourceStatusLayout.update(resource);
            this.addComponent(resourceStatusLayout);
            resources.add(resourceStatusLayout);
        }
    }
}
