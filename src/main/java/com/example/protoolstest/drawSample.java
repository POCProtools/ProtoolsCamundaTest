package com.example.protoolstest;

import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Named;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Named
@Component
public class drawSample implements JavaDelegate {
    static final Logger LOGGER = LoggerFactory.getLogger(drawSample.class);
    public drawSample() {
    }

    /**
     *
     * @param delegateExecution
     * @throws Exception
     */
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String sampleSize = (String) delegateExecution.getVariable("sampleSize");
        int sampleSizeInt = Integer.parseInt(sampleSize);
        if (sampleSizeInt > 4){
            String respnse = getSample(sampleSizeInt);
            delegateExecution.setVariable("body",respnse);
        }else {
            throw new BpmnError("sampleSize_low","The sample size must be greater than 5");
        }
    }

    /**
     *
     * @param sampleSize
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
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
