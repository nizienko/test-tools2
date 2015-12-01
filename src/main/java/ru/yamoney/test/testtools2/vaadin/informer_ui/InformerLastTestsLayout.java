package ru.yamoney.test.testtools2.vaadin.informer_ui;

import com.vaadin.ui.VerticalLayout;
import ru.yamoney.test.testtools2.common.Application;
import ru.yamoney.test.testtools2.info.Informer;
import ru.yamoney.test.testtools2.testmanager.TestExecution;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by def on 01.12.15.
 */
public class InformerLastTestsLayout extends VerticalLayout {
    private final Informer informer;
    private final List<TestResultLayout> testResultLayouts;
    private TestExecution lastTestExecution;
    public InformerLastTestsLayout() {
        super();
        this.setWidth("100%");
        this.setHeight(null);
        informer = (Informer) Application.getCtx().getBean("informer");
        testResultLayouts = new LinkedList<>();
    }

    public void update(){
        for (TestExecution testExecution : informer.getExecutions(lastTestExecution)){
            TestResultLayout testResultLayout = new TestResultLayout(testExecution);
            lastTestExecution = testExecution;
            testResultLayouts.add(testResultLayout);
            this.addComponentAsFirst(testResultLayout);
            if (testResultLayouts.size() > 100) {
                this.removeComponent(testResultLayouts.get(0));
                testResultLayouts.remove(0);
            }
        }
    }
}
