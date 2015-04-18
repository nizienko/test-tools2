package ru.yamoney.test.testtools2.rest;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import ru.yamoney.test.testtools2.common.Application;
import ru.yamoney.test.testtools2.db.DaoContainer;
import ru.yamoney.test.testtools2.testmanager.TestConfiguration;
import ru.yamoney.test.testtools2.testmanager.TestSetting;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by def on 19.04.15.
 */
@Path("/settings")
public class TestSettingsService {
    public static final Logger LOG = Logger.getLogger(TestExecutionService.class);

    @GET
    @Path("/{tk}")
    @Produces(MediaType.APPLICATION_XML)
    public Response getSettings(
            @PathParam("tk") String tk,
            @QueryParam(value = "contains")
            String contains
    ){
        LOG.info("tk=" + tk);
        LOG.info("contains=" + contains);
        return Response.status(200).entity(buildXMLAnswer(this.getXmlSettings(tk, contains))).build();
    }


    private List<TestSetting> loadSettings(String testConfigurationName, String contains) {
        DaoContainer daoContainer = (DaoContainer) Application.getCtx().getBean("daoContainer");

        TestConfiguration testConfiguration;
        try {
            testConfiguration = daoContainer.getTestSettingDao().selectTestConfigurationByName(testConfigurationName);
            List<TestSetting> testSettings;
            if (contains == null) {
                testSettings = daoContainer.getTestSettingDao().selectByTestConfiguration(testConfiguration);
            } else {
                testSettings = daoContainer.getTestSettingDao().selectByTestConfigurationContains(testConfiguration, contains);
            }
            return testSettings;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Document getXmlSettings(String testConfiguration, String contains) {
        Element root = new Element("TestTools");
        Element mainNode = new Element("Settings");
        root.addContent(mainNode);
        Document doc = new Document(root);
        List<TestSetting> testSettings = new ArrayList<TestSetting>();
        try {
            testSettings = loadSettings(testConfiguration, contains);
            mainNode.setAttribute("testconfiguration", testConfiguration);
            for (TestSetting testSetting : testSettings) {
                Element node = new Element("Setting");
                node.setAttribute("name", testSetting.getParameterName());
                node.setText(testSetting.getValue());
                mainNode.addContent(node);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            throw new IllegalArgumentException();
        }
        return doc;
    }

    private Document getErrorMessage(String error) {
        Element root = new Element("TestTools");
        Element mainNode = new Element("Error");
        root.addContent(mainNode);
        Document doc = new Document(root);
        mainNode.setText(error);
        return doc;
    }
    protected String buildXMLAnswer(Document doc) {
        StringWriter result = new StringWriter();
        try {

            XMLOutputter serializer = new XMLOutputter();
            serializer.setFormat(Format.getPrettyFormat());
            serializer.output(doc, result);
        } catch (IOException e) {
            System.err.println(e);
        }
        return result.toString();
    }
}