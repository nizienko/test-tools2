package ru.yamoney.test.testtools2.postponecheck.response_handler;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by def on 29.09.15.
 */
public class ResponseHandler {

    private final String script;
    private final String response;
    private final Map<String, String> parameters;
    private boolean status;
    private final ScriptEngine engine;


    public ResponseHandler(String script, String response, Map<String, String> parameters) {
        this.script = script;
        this.response = response;
        this.parameters = parameters;
        this.engine = new ScriptEngineManager().getEngineByName("nashorn");
    }

    public void proccessResponse() throws ScriptException, NoSuchMethodException {
        loadParameters();
        engine.eval(script);

        Invocable invocable = (Invocable) engine;

        Object result = invocable.invokeFunction("check", response);

        Map<String, String> newParams = new HashMap<>();
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            newParams.put(entry.getKey(), (String) engine.get(entry.getKey()));
        }
        parameters.putAll(newParams);

        if ("passed".equals(result)) {
            status = true;
        } else {
            status = false;
        }
    }

    private void loadParameters() {
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            engine.put(entry.getKey(), entry.getValue());
        }
    }

    public boolean isStatus() {
        return status;
    }
}
