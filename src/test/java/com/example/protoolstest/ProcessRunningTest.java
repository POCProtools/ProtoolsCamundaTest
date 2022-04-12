package com.example.protoolstest;

import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.*;
import static org.assertj.core.api.Assertions.*;

import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.extension.junit5.test.ProcessEngineExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.runtimeService;

@ExtendWith(ProcessEngineExtension.class)
public class ProcessRunningTest {
    static final Logger LOGGER = LoggerFactory.getLogger(createSurvey.class);
    @Test
    @Deployment(resources = {"testProcess.bpmn"})
    public void shouldExecuteProcess(){
        // Given we create a new process instance
        ProcessInstance processInstance = runtimeService().startProcessInstanceByKey("testProcess");
        LOGGER.info("Process Started");
        // Then it should be active
        assertThat(processInstance).isActive();
        // And there should exist just a single task within that process instance
        assertThat(task(processInstance)).isNotNull();
        // When we complete that task
        LOGGER.info("Complete Process");
        complete(task(processInstance));

        // Then the process instance should be ended
        LOGGER.info("Test Process Ended");
        assertThat(processInstance).isEnded();
    }
}
