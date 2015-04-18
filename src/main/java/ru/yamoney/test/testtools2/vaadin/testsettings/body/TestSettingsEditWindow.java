package ru.yamoney.test.testtools2.vaadin.testsettings.body;


import com.vaadin.ui.*;
import org.springframework.dao.EmptyResultDataAccessException;
import ru.yamoney.test.testtools2.common.Application;
import ru.yamoney.test.testtools2.db.DaoContainer;
import ru.yamoney.test.testtools2.testmanager.TestConfiguration;
import ru.yamoney.test.testtools2.testmanager.TestParameter;
import ru.yamoney.test.testtools2.testmanager.TestSetting;

/**
 * Created by nizienko on 12.11.14.
 */
public class TestSettingsEditWindow extends Window {
    DaoContainer daoContainer;
    TestSetting testSetting;

    public TestSettingsEditWindow(final TestConfiguration testConfiguration, String parameter) {
        super(testConfiguration.getName());
        daoContainer = (DaoContainer) Application.getCtx().getBean("daoContainer");
        try {
            testSetting = daoContainer.getTestSettingDao().selectTestSetting(testConfiguration, parameter);
        } catch (EmptyResultDataAccessException e) {
            TestParameter testParameter = daoContainer.getTestSettingDao().selectParameter(parameter);
            daoContainer.getTestSettingDao().insertValue(testConfiguration, testParameter, "");
            testSetting = daoContainer.getTestSettingDao().selectTestSetting(testConfiguration, parameter);
        }
        center();
        setWidth("400px");
        VerticalLayout content = new VerticalLayout();
        setContent(content);
        final TextField parameterTextField = new TextField();
        parameterTextField.setValue(testSetting.getParameterName());
        parameterTextField.setReadOnly(true);
        parameterTextField.setWidth("100%");
        final TextField valueTextField = new TextField();
        valueTextField.setValue(testSetting.getValue());
        content.addComponent(parameterTextField);
        content.addComponent(valueTextField);
        content.addComponent(new Label(testSetting.getDescription()));
        valueTextField.setWidth("100%");
        Button save = new Button("Save");
        content.addComponent(save);
        save.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent clickEvent) {
                try {
                    testSetting.setValue(valueTextField.getValue());
                    daoContainer.getTestSettingDao().updateValue(testConfiguration, testSetting);
                    close();
                    Notification.show(testSetting.getParameterName() + "=" + testSetting.getValue(),
                            Notification.Type.TRAY_NOTIFICATION);
                } catch (Exception e) {
                    e.printStackTrace();
                    Notification.show("Some error", Notification.Type.TRAY_NOTIFICATION);
                }
            }
        });
    }

}
