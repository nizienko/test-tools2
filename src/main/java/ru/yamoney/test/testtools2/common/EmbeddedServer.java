package ru.yamoney.test.testtools2.common;

import org.apache.log4j.Logger;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.webapp.WebAppContext;
import org.springframework.beans.FatalBeanException;

/**
 * Created by def on 31.03.15.
 */
public class EmbeddedServer extends AbstractDaemon implements ApplicationThread{
    public static final Logger LOG = Logger.getLogger(Application.class);
    private int port;
    private String webXml;
    private Server server;
    private boolean isRunning;

    public EmbeddedServer(Integer period) {
        super(period);
    }

    public void init() {
        if (Application.isEmbeddedServerMode()){
            Application.addThread(this);
        }
        else {
            LOG.info("No need to run " + this);
        }
    }

    public void process() {
        if (!isRunning) {
            try {
                LOG.info("stopping embedded server");
                server.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
            super.stop();
        }
    }

    @Override
    public void start() {
        isRunning = true;
        server = new Server();
        Connector connector = new SelectChannelConnector();
        connector.setPort(port);
        server.addConnector(connector);
        WebAppContext root = new WebAppContext(webXml, "/");
        server.setHandlers(new Handler[]{root});
        try {
            server.start();
            LOG.info("Embedded server started on port " + port);
        }
        catch (Exception e) {
            throw new IllegalStateException(e.getMessage() + " when starting embedded server");
        }
        super.start();
    }

    @Override
    public void stop() {
        LOG.info("Need to stop " + this);
        isRunning = false;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setWebXml(String webXml) {
        this.webXml = webXml;
    }

    public String toString(){

        return "Embedded server";
    }

}
