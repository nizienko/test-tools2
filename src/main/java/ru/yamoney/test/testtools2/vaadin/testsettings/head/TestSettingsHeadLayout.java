package ru.yamoney.test.testtools2.vaadin.testsettings.head;


import com.vaadin.data.Property;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import org.springframework.jdbc.UncategorizedSQLException;
import ru.yamoney.test.testtools2.common.Application;
import ru.yamoney.test.testtools2.db.DaoContainer;
import ru.yamoney.test.testtools2.testmanager.TestConfiguration;
import ru.yamoney.test.testtools2.vaadin.testsettings.body.AddNewParameterWindow;
import ru.yamoney.test.testtools2.vaadin.testsettings.body.AddNewTestConfigurationWindow;
import ru.yamoney.test.testtools2.vaadin.testsettings.body.EditParametersWindow;
import ru.yamoney.test.testtools2.vaadin.testsettings.body.TestSettingsBodyLayout;


/**
 * Created by def on 12.11.14.
 */
public class TestSettingsHeadLayout extends HorizontalLayout {
    private DaoContainer daoContainer;
    private MySelect tkSelect;
    private TestConfiguration currentTestConfiguration;
    private TestSettingsBodyLayout testSettingsBodyLayout;
    private TextField parameterField;
    private Button updateButton;
    private Button addNewParameterButton;
    private Button editParametersButton;


    public TestSettingsHeadLayout(final TestSettingsBodyLayout testSettingsBodyLayout) {
        this.testSettingsBodyLayout = testSettingsBodyLayout;
        daoContainer = (DaoContainer) Application.getCtx().getBean("daoContainer");
        tkSelect = new MySelect();
        tkSelect.setNullSelectionAllowed(true);
        this.addComponent(tkSelect);
        tkSelect.focus();
        parameterField = new TextField();
        this.addComponent(parameterField);
        updateButton = new Button("Update");
        this.addComponent(updateButton);
        updateButton.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent clickEvent) {
                if (currentTestConfiguration != null) {
                    updateTKList();
                    if ("".equals(parameterField.getValue())) {
                        testSettingsBodyLayout.updateTestSettings(currentTestConfiguration, daoContainer.getTestSettingDao().selectByTestConfiguration(currentTestConfiguration));
                    } else {
                        testSettingsBodyLayout.updateTestSettings(currentTestConfiguration, daoContainer.getTestSettingDao().selectByTestConfigurationContains(currentTestConfiguration, parameterField.getValue()));
                    }
                } else {
                    testSettingsBodyLayout.clear();
                }
            }
        });
        addNewParameterButton = new Button("New parameter");
        this.addComponent(addNewParameterButton);
        addNewParameterButton.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent clickEvent) {
                UI.getCurrent().addWindow(new AddNewParameterWindow());
            }
        });
        editParametersButton = new Button("Edit parameters");
        this.addComponent(editParametersButton);
        editParametersButton.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent clickEvent) {
                UI.getCurrent().addWindow(new EditParametersWindow());
            }
        });
        updateTKList();
        tkSelect.addValueChangeListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                String chosenTK;
                try {
                    chosenTK = valueChangeEvent.getProperty().getValue().toString();
                    if ("add new".equals(chosenTK)) {
                        UI.getCurrent().addWindow(new AddNewTestConfigurationWindow());
                    } else {
                        currentTestConfiguration = daoContainer.getTestSettingDao().selectTestConfigurationByName(chosenTK);
                        if ("".equals(parameterField.getValue())) {
                            testSettingsBodyLayout.updateTestSettings(currentTestConfiguration, daoContainer.getTestSettingDao().selectByTestConfiguration(currentTestConfiguration));
                        } else {
                            testSettingsBodyLayout.updateTestSettings(currentTestConfiguration, daoContainer.getTestSettingDao().selectByTestConfigurationContains(currentTestConfiguration, parameterField.getValue()));
                        }
                    }
                } catch (NullPointerException e) {
                    testSettingsBodyLayout.clear();
                }
            }
        });
    }

    private void updateTKList() {
        try {
            for (TestConfiguration tk : daoContainer.getTestSettingDao().selectTestConfigurations()) {
                tkSelect.addItem(tk.getName());
            }
            tkSelect.addItem("add new");
        } catch (UncategorizedSQLException e) {
        }
    }
}
