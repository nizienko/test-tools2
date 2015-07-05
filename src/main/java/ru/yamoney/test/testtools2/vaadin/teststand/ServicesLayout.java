package ru.yamoney.test.testtools2.vaadin.teststand;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import ru.yamoney.test.testtools2.common.Application;
import ru.yamoney.test.testtools2.common.DaoContainer;
import ru.yamoney.test.testtools2.teststand.services.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by def on 05.07.15.
 */
public class ServicesLayout extends VerticalLayout {
    private List<Service> services;

    public ServicesLayout(){
        this.setSizeFull();

        DaoContainer daoContainer = (DaoContainer) Application.getCtx().getBean("daoContainer");
        services = daoContainer.getServiceDao().getServices();

        for (final Service service : services) {
            Button button = new Button(service.getName());
            button.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    UI.getCurrent().addWindow(new ServiceWindow(service));
                }
            });
            this.addComponent(button);
        }
    }

    private class ServiceWindow extends Window {
        public ServiceWindow(final Service service){
            super(service.getName());
            final Map<String, TextField> params = new HashMap<>();
            String width = "600px";
            center();
            setWidth(width);
            final HorizontalLayout mainLayout = new HorizontalLayout();
            final VerticalLayout content = new VerticalLayout();
            setContent(mainLayout);
            mainLayout.addComponent(content);
            final Label resultLabel = new Label("Answer");
            resultLabel.setContentMode(ContentMode.HTML);
            mainLayout.addComponent(resultLabel);
            for (NameValuePair nvp: service.getEditableParams()) {
                TextField textField = new TextField(nvp.getName());
                textField.setValue(nvp.getValue());
                content.addComponent(textField);
                params.put(nvp.getName(), textField);
            }
            Button button = new Button("Send");
            button.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    List<NameValuePair> newParams = new ArrayList<NameValuePair>();
                    for (NameValuePair nvp: service.getEditableParams()){
                        newParams.add(new BasicNameValuePair(nvp.getName(), params.get(nvp.getName()).getValue()));
                    }
                    service.setEditableParams(newParams);
                    String result = service.sendRequest();
                    resultLabel.setValue(result);
                    Notification.show(result, Notification.Type.TRAY_NOTIFICATION);
                }
            });
            content.addComponent(button);

        }
    }

}
