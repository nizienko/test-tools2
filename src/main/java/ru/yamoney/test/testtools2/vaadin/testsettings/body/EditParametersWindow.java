package ru.yamoney.test.testtools2.vaadin.testsettings.body;


import com.vaadin.data.Property;
import com.vaadin.ui.*;
import org.springframework.dao.EmptyResultDataAccessException;
import ru.yamoney.test.testtools2.common.Application;
import ru.yamoney.test.testtools2.common.DaoContainer;
import ru.yamoney.test.testtools2.testmanager.TestParameter;

/**
 * Created by nizienko on 13.11.14.
 */
public class EditParametersWindow extends Window {
    private final String width = "600px";
    private final String width2 = "500px";
    private DaoContainer daoContainer;
    private Table table;
    private Button updateButton;
    private CheckBox really;


    public EditParametersWindow() {
        super("Edit parameters");
        daoContainer = (DaoContainer) Application.getCtx().getBean("daoContainer");
        center();
        setWidth(width);
        VerticalLayout content = new VerticalLayout();
        setContent(content);
        updateButton = new Button("Update");
        content.addComponent(updateButton);
        table = new Table();
        table.setWidth("100%");
        table.setHeight("100%");
        table.setEditable(false);
        table.setSelectable(true);
        table.setImmediate(true);
        table.addContainerProperty("Parameter", String.class, null);
        table.addContainerProperty("Description", String.class, null);
        content.addComponent(table);
        this.update();
        table.addValueChangeListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                try {
                    String parameter = (String) table.getContainerProperty(table.getValue(), "Parameter").getValue();
                    UI.getCurrent().addWindow(new EditParameterWindow(parameter));
                } catch (NullPointerException e) {
                }
            }
        });
        updateButton.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent clickEvent) {
                update();
            }
        });

    }

    private void update() {
        int i = 1;
        table.removeAllItems();
        for (TestParameter tp : daoContainer.getTestSettingDao().selectAllParameters()) {
            table.addItem(new Object[]{
                    tp.getName(),
                    tp.getDescription()
            }, new Integer(i));
            i++;
        }
    }

    private class EditParameterWindow extends Window {
        TestParameter testParameter;

        public EditParameterWindow(String parameter) {
            super(parameter);
            try {
                testParameter = daoContainer.getTestSettingDao().selectParameter(parameter);
            } catch (EmptyResultDataAccessException e) {
                Notification.show("No such parameter, click update",
                        Notification.Type.ERROR_MESSAGE);
            }
            center();
            setWidth(width2);
            VerticalLayout content = new VerticalLayout();
            setContent(content);
            final TextField name = new TextField("Name");
            name.setValue(testParameter.getName());
            final TextField description = new TextField("Description");
            description.setValue(testParameter.getDescription());
            name.setWidth(width2);
            description.setWidth(width2);
            Button save = new Button("Save");
            content.addComponent(name);
            content.addComponent(description);
            HorizontalLayout horizontalLayout = new HorizontalLayout();
            horizontalLayout.addComponent(save);
            content.addComponent(horizontalLayout);
            save.addClickListener(new Button.ClickListener() {
                public void buttonClick(Button.ClickEvent clickEvent) {
                    try {
                        if (!"".equals(name.getValue())) {
                            testParameter.setName(name.getValue());
                            testParameter.setDescription(description.getValue());
                            daoContainer.getTestSettingDao().updateParameter(testParameter);
                            close();
                            Notification.show(name.getValue() + " updated",
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
            Button delete = new Button("Delete");
            delete.addClickListener(new Button.ClickListener() {
                public void buttonClick(Button.ClickEvent clickEvent) {
                    if (really.getValue()) {
                        try {
                            daoContainer.getTestSettingDao().deleteParameter(testParameter);
                            Notification.show(name.getValue() + " deleted",
                                    Notification.Type.TRAY_NOTIFICATION);
                            close();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Notification.show("Error", Notification.Type.ERROR_MESSAGE);
                        }
                    } else {
                        Notification.show("You are not sure",
                                Notification.Type.WARNING_MESSAGE);
                    }

                }
            });
            horizontalLayout.addComponent(delete);
            really = new CheckBox("I'm sure!");
            really.setValue(false);
            horizontalLayout.addComponent(really);

        }
    }
}
