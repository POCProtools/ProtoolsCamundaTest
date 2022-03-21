package com.example.protoolstest;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Named;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Named
public class createAccount implements JavaDelegate {
    static final Logger LOGGER = LoggerFactory.getLogger(createAccount.class);
    public createAccount(){}
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String sample = (String) delegateExecution.getVariable("body");
        String surveyID = (String) delegateExecution.getVariable("surveyID");
        int responseCodeCreateAccount = getNewAccounts(sample);
        delegateExecution.setVariable("respCodeCreateAccount",responseCodeCreateAccount);

    }

    public int getNewAccounts(String body) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://annuaire.dev.insee.io/comptes"))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        LOGGER.info(response.body());
        return(response.statusCode());
    }
}
