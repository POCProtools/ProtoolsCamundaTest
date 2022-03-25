package com.example.protoolstest;

import camundajar.impl.com.google.gson.JsonObject;
import camundajar.impl.com.google.gson.reflect.TypeToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Named;
import javax.ws.rs.core.HttpHeaders;
import java.io.IOException;
import java.lang.reflect.Type;
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

    /**
     *
     * @param sample
     * @param surveyID
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public int addToSurvey(String sample, String surveyID) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        Gson gson = new Gson();
        Person[] map = gson.fromJson(sample,Person[].class);

        LOGGER.info("add to survey : " + map.toString());
        int statusCode = 0;
        for (Person person: map){
            var id = person.getId();
            var values = new HashMap<String, String>() {{
                put("email", person.getEmail());
                put ("nom", person.getNom());
                put("prenom", person.getPrenom());
                put("telephone", person.getTelephone());
                put("id_survey",surveyID);
            }};
            var objectMapper = new ObjectMapper();
            String requestBody = objectMapper
                    .writeValueAsString(values);
            requestBody = "[" + requestBody + "]";
            LOGGER.info("add unit to survey: "+ requestBody);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://coleman.dev.insee.io/surveys/"+ surveyID+"/units"))
                    .setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();
            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());
            LOGGER.info("https://coleman.dev.insee.io/persons/"+ id);
            LOGGER.info(response.body());
            statusCode = response.statusCode();
        }


        return (statusCode);
    }

    /**
     *
     * @param object
     * @return
     * @throws JSONException
     */
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

    /**
     *
     * @param array
     * @return
     * @throws JSONException
     */
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
