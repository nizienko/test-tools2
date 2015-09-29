import org.junit.Test;
import ru.yamoney.test.testtools2.postponecheck.response_handler.ResponseHandler;

import javax.script.ScriptException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by def on 29.09.15.
 */
public class ResponseHandlerTest {

    @Test
    public void processResponse() throws ScriptException, NoSuchMethodException {
        final String script = "" +
                "var check = function() {\n" +
                "    print('test = ' + test + '\\n');\n" +
                "    test = 'changed value';\n" +
                "    return test;\n" +
                "};" +
                "";
        final String response = "";
        final Map<String, String> parameters = new HashMap<>();
        parameters.put("test", "ahahaha");

        System.out.println(parameters);

        ResponseHandler responseHandler = new ResponseHandler(script, response, parameters);

        responseHandler.proccessResponse();

        System.out.println(parameters);

    }

    @Test
    public void parseXml() throws ScriptException, NoSuchMethodException {
        final String script = "" +
                "var check = function(xml) {\n" +
                "\tvar xmlDoc=xml.getDocumentElement();\n" +
                "\tnumber = xmlDoc.getElementsByTagName(\"streetNumber\")[0].childNodes[0].nodeValue;\n" +
                "\tstreet = xmlDoc.getElementsByTagName(\"street\")[0].childNodes[0].nodeValue;\n" +
                "\treturn 'passed';\n" +
                "};" +
                "";
        final String response = "" +
                "<address>\n" +
                " <street>Roble Ave</street>\n" +
                "  <mtfcc>S1400</mtfcc>\n" +
                "  <streetNumber>649</streetNumber>\n" +
                "  <lat>37.45127</lat>\n" +
                "  <lng>-122.18032</lng>\n" +
                "  <distance>0.04</distance>\n" +
                "  <postalcode>94025</postalcode>\n" +
                "  <placename>Menlo Park</placename>\n" +
                "  <adminCode2>081</adminCode2>\n" +
                "  <adminName2>San Mateo</adminName2>\n" +
                "  <adminCode1>CA</adminCode1>\n" +
                "  <adminName1>California</adminName1>\n" +
                "  <countryCode>US</countryCode>\n" +
                " </address>" +
                "";
        final Map<String, String> parameters = new HashMap<>();
        parameters.put("number", "");
        parameters.put("street", "");

        System.out.println(parameters);

        ResponseHandler responseHandler = new ResponseHandler(script, response, parameters);
        responseHandler.proccessResponse();

        System.out.println(parameters);

    }

    @Test
    public void parseJson() throws ScriptException, NoSuchMethodException {
        final String script = "" +
                "var check = function(inputText) {\n" +
                "\tvar jsonObject=new org.json.JSONObject(inputText);\n" +
                "\tfirstname = jsonObject.get(\"firstname\");\n" +
                "\tnumber = jsonObject.get(\"lastname\");\n" +
                "\treturn 'passed';\n" +
                "};" +
                "";
        final String response = "" +
                "{\n" +
                "  \"firstname\":\"Some\",\n" +
                "  \"lastname\":\"One\",\n" +
                "  \"petNames\":[\"Fluffy\",\"Pickle\"],\n" +
                "  \"favouriteNumber\":5\n" +
                "}" +
                "";
        final Map<String, String> parameters = new HashMap<>();
        parameters.put("firstname", "");
        parameters.put("lastname", "");

        System.out.println(parameters);

        ResponseHandler responseHandler = new ResponseHandler(script, response, parameters);
        responseHandler.proccessResponse();

        System.out.println(parameters);

    }
}
