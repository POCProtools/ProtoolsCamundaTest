package com.example.protoolstest;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.extension.junit5.test.ProcessEngineExtension;
import org.camunda.bpm.scenario.ProcessScenario;
import org.camunda.bpm.scenario.Scenario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;

import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.withVariables;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(ProcessEngineExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EntireProcessTest {
    DelegateExecution execution;
    HashMap<String, Object> variables = new HashMap<>();

    @InjectMocks
    private drawSample drawSample;
    private createSurvey createSurvey;
    private createAccount createAccount;
    private addToSurvey addToSurvey;

    @Mock
    private ProcessScenario scenario;

    @BeforeEach
    void setUp() { // Setup de l'environement d'exécution mock
        MockitoAnnotations.openMocks(this);
        variables.put("sampleSize", "8");
        variables.put("surveyName", "TestMockSurvey");
        variables.put("dateDeb", "01/10/2022");

        when(scenario.waitsAtUserTask("ErrorTaskSampleSize")).thenReturn((task) -> {
            task.complete();
        });

        when(scenario.waitsAtUserTask("createSurvey")).thenReturn((task) -> task.complete(withVariables("surveyName", "TestMockSurvey","dateDeb", "01/10/2022")));

        when(scenario.waitsAtUserTask("SelectSampleSize")).thenReturn((task) ->
                task.complete(withVariables("sampleSize", "8")));

        when(scenario.waitsAtUserTask("SetState")).thenReturn((task) -> {
            task.complete();
        });


    }

    @Test
    @Deployment(resources = {"process.bpmn"})
    void TestHappyProcess() throws Exception{
        // Récupération de variables des task précédentes
        Scenario handler = Scenario.run(scenario).startByKey("protools-camunda-test-process", variables).execute();
        verify(scenario, atMostOnce()).hasCompleted("createSurvey");
        verify(scenario, atMostOnce()).hasCompleted("SaveNewSurvey");
        verify(scenario, atMostOnce()).hasCompleted("DrawSample");
        verify(scenario, atMostOnce()).hasCompleted("AddToSurvey");
        verify(scenario, atMostOnce()).hasCompleted("CreateAccounts");
        verify(scenario).hasFinished("Event_1n0yp34");

    }


}
