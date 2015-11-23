package ru.yamoney.test.testtools2.vaadin.users;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import ru.yamoney.test.testtools2.common.Application;
import ru.yamoney.test.testtools2.common.DaoContainer;
import ru.yamoney.test.testtools2.user_cashe.UserStatus;

import java.util.Map;

/**
 * Created by nizienko on 23.11.2015.
 */
public class CachedUsersLayout extends GridLayout {
    private Label label;
    private DaoContainer daoContainer;
    private Button updateButton;

    public CachedUsersLayout() {
        super(1, 3);
        this.setRowExpandRatio(2, 1);
        this.setSizeFull();
        this.daoContainer = (DaoContainer) Application.getCtx().getBean("daoContainer");

        updateButton = new Button("Load");
        updateButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                updatePage();
            }
        });

        label = new Label();
        label.setContentMode(ContentMode.HTML);
        this.addComponent(updateButton);
        this.addComponent(label);
    }

    private void updatePage() {
        final StringBuffer sb = new StringBuffer();
        sb.append("<h2>New users</h2>");
        addInfoTable(sb, UserStatus.NEW);
        sb.append("<h2>Old users</h2>");
        addInfoTable(sb, UserStatus.USED);
        label.setValue(sb.toString());
    }

    private void addInfoTable(StringBuffer sb, UserStatus userStatus) {
        sb.append("<table>");
        for (Map<String, Object> map : daoContainer.getUserDao().getCountOfUsers(userStatus)) {
            sb.append("<tr><td>" + map.get("host") + "</td><td>" + map.get("count") + "</td></tr>");
        }
        sb.append("</table>");
    }
}