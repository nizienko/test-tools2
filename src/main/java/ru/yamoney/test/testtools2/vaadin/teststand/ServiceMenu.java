package ru.yamoney.test.testtools2.vaadin.teststand;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import ru.yamoney.test.testtools2.common.Application;
import ru.yamoney.test.testtools2.common.DaoContainer;
import ru.yamoney.test.testtools2.teststand.services.Service;

import java.util.*;

/**
 * Created by def on 26.07.15.
 */
public class ServiceMenu extends MenuBar {
    private List<Service> services;

    public ServiceMenu(String name) {
        final DaoContainer daoContainer = (DaoContainer) Application.getCtx().getBean("daoContainer");
        services = daoContainer.getServiceDao().getServices();
        MenuBar.MenuItem serviceItem = this.addItem(name, null);

        for (final Service service : services) {
            MenuBar.MenuItem currentItem = serviceItem;
            String[] menuItems = service.getName().split("\\|");
            int level = 0;
            for (String menuItem : menuItems) {
                System.out.println(menuItem);
                boolean alreadyHas = false;
                try {
                    for (MenuBar.MenuItem item : currentItem.getChildren()) {
                        if (menuItem.equals(item.getText())) {
                            currentItem = item;
                            level++;
                            alreadyHas = true;
                            break;
                        }
                    }
                } catch (NullPointerException e) {
                }
                if (!alreadyHas) {
                    level++;
                    if (level == menuItems.length) {
                        currentItem.addItem(menuItem, new ServiceCommand(service));
                    } else {
                        MenuBar.MenuItem newItem = currentItem.addItem(menuItem, null);
                        currentItem = newItem;
                    }
                }
            }
        }
    }

    private class ServiceCommand implements MenuBar.Command {
        private Service service;

        public ServiceCommand(Service service) {
            this.service = service;
        }

        public void menuSelected(MenuBar.MenuItem selectedItem) {
            UI.getCurrent().addWindow(new ServiceWindow(service));
        }
    }

    private class ServiceWindow extends Window {
        public ServiceWindow(final Service service) {
            super(service.getName().replace("|", ". "));
            final Map<String, TextField> params = new HashMap<>();
            String width = "600px";
            center();
            setWidth(width);
            final HorizontalLayout mainLayout = new HorizontalLayout();
            final FormLayout content = new FormLayout();
            setContent(mainLayout);
            mainLayout.addComponent(content);
            final Label resultLabel = new Label();
            resultLabel.setContentMode(ContentMode.HTML);
            mainLayout.addComponent(resultLabel);
            for (final NameValuePair nvp : service.getEditableParams()) {
                TextField textField = new TextField(nvp.getName());
                textField.setValue(nvp.getValue());
                content.addComponent(textField);
                params.put(nvp.getName(), textField);
            }
            Button button = new Button("Send");
            button.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    resultLabel.setValue("<i>processing</i>");
                    List<NameValuePair> newParams = new ArrayList<NameValuePair>();
                    for (NameValuePair nvp : service.getEditableParams()) {
                        newParams.add(new BasicNameValuePair(nvp.getName(), params.get(nvp.getName()).getValue()));
                    }
                    service.setEditableParams(newParams);
                    String result = service.sendRequest();
                    resultLabel.setValue(createAnswer(result));
                }
            });
            content.addComponent(button);

        }

        private String createAnswer(String result) {
            try {
                StringBuffer answer = new StringBuffer();
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject != null) {
                    Iterator<String> keys = jsonObject.keys();
                    while (keys.hasNext()) {
                        String key = keys.next();
                        String val = null;
                        val = jsonObject.getString(key);
                        answer.append(key + ": " + val + "<br>");
                    }
                }
                return answer.toString();
            } catch (JSONException e) {
                return result;
            }
        }
    }

}
