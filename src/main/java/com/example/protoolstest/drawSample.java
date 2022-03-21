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
public class drawSample implements JavaDelegate {
    static final Logger LOGGER = LoggerFactory.getLogger(drawSample.class);
    public drawSample() {
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String sampleSize = (String) delegateExecution.getVariable("sampleSize");
        int sampleSizeInt = Integer.parseInt(sampleSize);
        String respnse = getSample(sampleSizeInt);
        delegateExecution.setVariable("body",respnse);
    }

    public String getSample(int sampleSize) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        String url = "https://crabe.dev.insee.io/persons/sample/" + String.valueOf(sampleSize);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        LOGGER.info(response.body());
        return (response.body());
    }

}
