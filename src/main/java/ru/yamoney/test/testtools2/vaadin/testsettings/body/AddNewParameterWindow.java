package ru.yamoney.test.testtools2.vaadin.testsettings.body;


import com.vaadin.ui.*;
import ru.yamoney.test.testtools2.common.Application;
import ru.yamoney.test.testtools2.common.DaoContainer;

/**
 * Created by nizienko on 13.11.14.
 */
public class AddNewParameterWindow extends Window {
    DaoContainer daoContainer;

    public AddNewParameterWindow() {
        super("New parameter");
        String width = "600px";
        daoContainer = (DaoContainer) Application.getCtx().getBean("daoContainer");
        center();
        setWidth(width);
        VerticalLayout content = new VerticalLayout();
        setContent(content);
        final TextField name = new TextField("Name");
        name.setWidth(width);
        final TextField description = new TextField("Description");
        description.setWidth(width);
        Button save = new Button("Save");
        content.addComponent(name);
        content.addComponent(description);
        content.addComponent(save);
        save.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent clickEvent) {
                try {
                    if (!"".equals(name.getValue())) {
                        daoContainer.getTestSettingDao().insertParameter(name.getValue(), description.getValue());
                        close();
                        Notification.show(name.getValue() + " added",
                                Notification.Type.TRAY_NOTIFICATION);
                    } else {
                        Notification.show("Type parameter name",
                                Notification.Type.ERROR_MESSAGE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Notification.show("Error", Notification.Type.ERROR_MESSAGE);
                }
            }
        });
    }

}
