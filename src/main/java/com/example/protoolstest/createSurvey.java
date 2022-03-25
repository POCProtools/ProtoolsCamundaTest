package com.example.protoolstest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Named;
import javax.ws.rs.core.HttpHeaders;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;

@Named
public class createSurvey implements JavaDelegate {
    static final Logger LOGGER = LoggerFactory.getLogger(createSurvey.class);

    /**
     *
     * @param delegateExecution
     * @throws Exception
     */
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String surveyName = (String) delegateExecution.getVariable("surveyName");
        String dateDeb = (String) delegateExecution.getVariable("dateDeb");
        String dateEnd = (String) delegateExecution.getVariable("dateEnd");
        String surveyID = saveSurvey(surveyName,dateDeb,dateEnd);
        delegateExecution.setVariable("surveyID", surveyID);
    }

    /**
     *
     * @param surveyName
     * @param dateDeb
     * @param dateEnd
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public String saveSurvey(String surveyName, String dateDeb, String dateEnd) throws IOException, InterruptedException {
        var values = new HashMap<String, String>() {{
            put("name", surveyName);
            put ("dateDeb", dateDeb);
            put("dateEnd", dateEnd);
        }};

        var objectMapper = new ObjectMapper();
        String requestBody = objectMapper
                .writeValueAsString(values);
        LOGGER.info(requestBody);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://coleman.dev.insee.io/surveys/"))
                .setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        LOGGER.info(response.body());

        JSONObject jsonResponse = new JSONObject(response.body());
        int idInt = jsonResponse.getInt("id");
        String idSurvey = String.valueOf(idInt);
        return idSurvey;

    }
}
