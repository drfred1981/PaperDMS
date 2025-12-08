package com.ged.workflow.domain;

import static com.ged.workflow.domain.WorkflowInstanceTestSamples.*;
import static com.ged.workflow.domain.WorkflowStepTestSamples.*;
import static com.ged.workflow.domain.WorkflowTaskTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ged.workflow.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WorkflowTaskTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkflowTask.class);
        WorkflowTask workflowTask1 = getWorkflowTaskSample1();
        WorkflowTask workflowTask2 = new WorkflowTask();
        assertThat(workflowTask1).isNotEqualTo(workflowTask2);

        workflowTask2.setId(workflowTask1.getId());
        assertThat(workflowTask1).isEqualTo(workflowTask2);

        workflowTask2 = getWorkflowTaskSample2();
        assertThat(workflowTask1).isNotEqualTo(workflowTask2);
    }

    @Test
    void instanceTest() {
        WorkflowTask workflowTask = getWorkflowTaskRandomSampleGenerator();
        WorkflowInstance workflowInstanceBack = getWorkflowInstanceRandomSampleGenerator();

        workflowTask.setInstance(workflowInstanceBack);
        assertThat(workflowTask.getInstance()).isEqualTo(workflowInstanceBack);

        workflowTask.instance(null);
        assertThat(workflowTask.getInstance()).isNull();
    }

    @Test
    void stepTest() {
        WorkflowTask workflowTask = getWorkflowTaskRandomSampleGenerator();
        WorkflowStep workflowStepBack = getWorkflowStepRandomSampleGenerator();

        workflowTask.setStep(workflowStepBack);
        assertThat(workflowTask.getStep()).isEqualTo(workflowStepBack);

        workflowTask.step(null);
        assertThat(workflowTask.getStep()).isNull();
    }
}
