package com.example.protoolstest;

import camundajar.impl.com.google.gson.JsonObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Named;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

@Named
public class addToSurvey implements JavaDelegate {
    static final Logger LOGGER = LoggerFactory.getLogger(drawSample.class);
    public addToSurvey() {
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String sample = (String) delegateExecution.getVariable("body");
        String surveyID = (String) delegateExecution.getVariable("surveyID");
        int respnse = addToSurvey(sample,surveyID);
        delegateExecution.setVariable("createAccountStatus",respnse);
    }

    public int addToSurvey(String sample, String surveyID) throws IOException, InterruptedException {
        Long surveyIDLong = Long.parseLong(surveyID);
        JSONObject json = new JSONObject(sample);

        Map<String, Object> retMap = new HashMap<String, Object>();
        retMap = toMap(json);

        LOGGER.info(retMap.toString());

        var objectMapper = new ObjectMapper();
        String requestBody = objectMapper
                .writeValueAsString(retMap);

        HttpClient client = HttpClient.newHttpClient();
        // A finir
        // TODO : Ajouter surveyID aux personnes
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://coleman.dev.insee.io/persons/"))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        LOGGER.info(response.body());

        return (response.statusCode());
    }

    public static Map<String, Object> toMap(JSONObject object) throws JSONException {
        Map<String, Object> map = new HashMap<String, Object>();

        Iterator<String> keysItr = object.keys();
        while(keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);

            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value);
        }
        return map;
    }

    public static List<Object> toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<Object>();
        for(int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }
}
