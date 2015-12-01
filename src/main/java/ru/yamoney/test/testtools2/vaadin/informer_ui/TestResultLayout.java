package ru.yamoney.test.testtools2.vaadin.informer_ui;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import ru.yamoney.test.testtools2.common.Utils;
import ru.yamoney.test.testtools2.testmanager.TestExecution;

import static ru.yamoney.test.testtools2.testmanager.ExecutionStatus.*;

/**
 * Created by def on 02.12.15.
 */
public class TestResultLayout extends Label {
    public TestResultLayout(TestExecution testExecution){
        this.setContentMode(ContentMode.HTML);

        String html;
        if (testExecution.getStatus() == PASSED.getIntegerValue()) {
            html = TEMPLATE.replace("${CLASS}", ONLINE_STYLE);
        }
        else {
            html = TEMPLATE.replace("${CLASS}", OFFLINE_STYLE);
        }
        html = html
                .replace("${ISSUE}", testExecution.getIssue())
                .replace("${NAME}", testExecution.getName())
                .replace("${COMMENT}", testExecution.getComment())
                .replace("${PROJECT}", testExecution.getProject())
                .replace("${VERSION}", testExecution.getVersion())
                .replace("${BUILD}", testExecution.getBuild())
                .replace("${EXECUTION}", testExecution.getExecution())
                .replace("${DT}", Utils.getDateFormat().format(testExecution.getExecutionDt()))
        ;
        this.setValue(html);
    }

    private final static String ONLINE_STYLE = "resource-online";
    private final static String OFFLINE_STYLE = "resource-offline";


    private final static String TEMPLATE = "" +
            "<table class=\"${CLASS}\" width=100%>\n" +
            "\t\t\t<tr>\n" +
            "\t\t\t\t<td width=10%>${ISSUE}</td>\n" +
            "\t\t\t\t<td width=90%>${NAME}</td>\t\n" +
            "\t\t\t</tr>\n" +
            "\t\t</table>" +
            "<table class=\"${CLASS}_little\" width=100%>\n" +
            "\t\t\t<tr>\n" +
            "\t\t\t\t<td width=30%>${PROJECT}-${VERSION}-${BUILD} ${EXECUTION}</td>\n" +
            "\t\t\t\t<td width=40%>${COMMENT}</td>\t\n" +
            "\t\t\t\t<td width=30%>${DT}</td>\t\n" +
            "\t\t\t</tr>\n" +
            "\t\t</table><br>";
}
