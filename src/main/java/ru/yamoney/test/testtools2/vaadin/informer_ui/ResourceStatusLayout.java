package ru.yamoney.test.testtools2.vaadin.informer_ui;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import ru.yamoney.test.testtools2.teststand.resources.Resource;

/**
 * Created by def on 01.12.15.
 */
public class ResourceStatusLayout extends Label {

    public ResourceStatusLayout(){
        this.setContentMode(ContentMode.HTML);
    }

    public void update(Resource resource){
        String currentHtml;
        if (resource.getStatus().isOnline()) {
            currentHtml = TEMPLATE.replace("${CLASS}", ONLINE_STYLE);
        }
        else {
            currentHtml = TEMPLATE.replace("${CLASS}", OFFLINE_STYLE);
        }
        currentHtml = currentHtml
                .replace("${NAME}", resource.toString())
                .replace("${COMMENT}", resource.getStatus().getStatusMeassage());
        if(!this.getValue().equals(currentHtml)) {
            this.setValue(currentHtml);
        }
    }

    private final static String ONLINE_STYLE = "resource-online";
    private final static String OFFLINE_STYLE = "resource-offline";


    private final static String TEMPLATE = "" +
            "<table class=\"${CLASS}\" width=100%>\n" +
            "\t\t\t<tr>\n" +
            "\t\t\t\t<td width=40%>${NAME}</td>\n" +
            "\t\t\t\t<td width=60%>${COMMENT}</td>\t\n" +
            "\t\t\t</tr>\n" +
            "\t\t</table>";
}
