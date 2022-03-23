package com.example.protoolstest;

import camundajar.impl.com.google.gson.reflect.TypeToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.gson.*;
import javax.inject.Named;
import javax.ws.rs.core.HttpHeaders;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Named
public class createAccount implements JavaDelegate {
    static final Logger LOGGER = LoggerFactory.getLogger(createAccount.class);
    public createAccount(){}
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String sample = (String) delegateExecution.getVariable("body");
        String surveyID = (String) delegateExecution.getVariable("surveyID");
        int responseCodeCreateAccount = getNewAccounts(sample,surveyID);
        delegateExecution.setVariable("respCodeCreateAccount",responseCodeCreateAccount);

    }

    public int getNewAccounts(String sample, String surveyID) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        Gson gson = new Gson();
        Person[] map = gson.fromJson(sample,Person[].class);

        LOGGER.info("Create account : " + map.toString());

        for (Person person: map) {
            var values = new HashMap<String, Object>() {{
                put("email", person.getEmail());
                put("nom", person.getNom());
                put("prenom", person.getPrenom());
                put("telephone", person.getTelephone());
                put("id_survey",Long.parseLong(surveyID));
            }};
            var objectMapper = new ObjectMapper();
            String requestBody = objectMapper
                    .writeValueAsString(values);
            requestBody = "[" + requestBody + "]";
            LOGGER.info("Create account for unit: " + requestBody);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://annuaire.dev.insee.io/comptes/"))
                    .setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();
            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());

            LOGGER.info(response.body());
        }
        return(200);
    }
}
