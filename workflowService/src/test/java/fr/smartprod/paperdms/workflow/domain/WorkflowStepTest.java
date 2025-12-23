package fr.smartprod.paperdms.workflow.domain;

import static fr.smartprod.paperdms.workflow.domain.WorkflowStepTestSamples.*;
import static fr.smartprod.paperdms.workflow.domain.WorkflowTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.workflow.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WorkflowStepTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkflowStep.class);
        WorkflowStep workflowStep1 = getWorkflowStepSample1();
        WorkflowStep workflowStep2 = new WorkflowStep();
        assertThat(workflowStep1).isNotEqualTo(workflowStep2);

        workflowStep2.setId(workflowStep1.getId());
        assertThat(workflowStep1).isEqualTo(workflowStep2);

        workflowStep2 = getWorkflowStepSample2();
        assertThat(workflowStep1).isNotEqualTo(workflowStep2);
    }

    @Test
    void workflowTest() {
        WorkflowStep workflowStep = getWorkflowStepRandomSampleGenerator();
        Workflow workflowBack = getWorkflowRandomSampleGenerator();

        workflowStep.setWorkflow(workflowBack);
        assertThat(workflowStep.getWorkflow()).isEqualTo(workflowBack);

        workflowStep.workflow(null);
        assertThat(workflowStep.getWorkflow()).isNull();
    }
}
