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
 * Created by def on 05.07.15.
 */
public class ServicesLayout extends VerticalLayout {

    public ServicesLayout() {
        this.setSizeFull();
        this.setSpacing(false);

        this.addComponent(new ServiceMenu());

    }
}
