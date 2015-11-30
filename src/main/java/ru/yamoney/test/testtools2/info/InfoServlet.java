package ru.yamoney.test.testtools2.info;

import org.json.JSONArray;
import ru.yamoney.test.testtools2.common.Application;
import ru.yamoney.test.testtools2.testmanager.TestExecution;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.acl.LastOwnerException;

/**
 * Created by def on 27.11.15.
 */
public class InfoServlet extends HttpServlet {
    private Informer informer;

    public void init() throws ServletException {
        informer = (Informer) Application.getCtx().getBean("informer");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JSONArray array = new JSONArray();
        for (TestExecution testExecution : informer.getExecutions()) {
            array.put(testExecution.getJSON());
        }
        System.out.println(array.toString());
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setContentType("text/event-stream");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(array.toString());
        response.getWriter().flush();
    }
}
