package fr.smartprod.paperdms.workflow.domain;

import static fr.smartprod.paperdms.workflow.domain.WorkflowInstanceTestSamples.*;
import static fr.smartprod.paperdms.workflow.domain.WorkflowStepTestSamples.*;
import static fr.smartprod.paperdms.workflow.domain.WorkflowTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.workflow.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class WorkflowTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Workflow.class);
        Workflow workflow1 = getWorkflowSample1();
        Workflow workflow2 = new Workflow();
        assertThat(workflow1).isNotEqualTo(workflow2);

        workflow2.setId(workflow1.getId());
        assertThat(workflow1).isEqualTo(workflow2);

        workflow2 = getWorkflowSample2();
        assertThat(workflow1).isNotEqualTo(workflow2);
    }

    @Test
    void workflowStpesTest() {
        Workflow workflow = getWorkflowRandomSampleGenerator();
        WorkflowStep workflowStepBack = getWorkflowStepRandomSampleGenerator();

        workflow.addWorkflowStpes(workflowStepBack);
        assertThat(workflow.getWorkflowStpes()).containsOnly(workflowStepBack);
        assertThat(workflowStepBack.getWorkflow()).isEqualTo(workflow);

        workflow.removeWorkflowStpes(workflowStepBack);
        assertThat(workflow.getWorkflowStpes()).doesNotContain(workflowStepBack);
        assertThat(workflowStepBack.getWorkflow()).isNull();

        workflow.workflowStpes(new HashSet<>(Set.of(workflowStepBack)));
        assertThat(workflow.getWorkflowStpes()).containsOnly(workflowStepBack);
        assertThat(workflowStepBack.getWorkflow()).isEqualTo(workflow);

        workflow.setWorkflowStpes(new HashSet<>());
        assertThat(workflow.getWorkflowStpes()).doesNotContain(workflowStepBack);
        assertThat(workflowStepBack.getWorkflow()).isNull();
    }

    @Test
    void workflowInstancesTest() {
        Workflow workflow = getWorkflowRandomSampleGenerator();
        WorkflowInstance workflowInstanceBack = getWorkflowInstanceRandomSampleGenerator();

        workflow.addWorkflowInstances(workflowInstanceBack);
        assertThat(workflow.getWorkflowInstances()).containsOnly(workflowInstanceBack);
        assertThat(workflowInstanceBack.getWorkflow()).isEqualTo(workflow);

        workflow.removeWorkflowInstances(workflowInstanceBack);
        assertThat(workflow.getWorkflowInstances()).doesNotContain(workflowInstanceBack);
        assertThat(workflowInstanceBack.getWorkflow()).isNull();

        workflow.workflowInstances(new HashSet<>(Set.of(workflowInstanceBack)));
        assertThat(workflow.getWorkflowInstances()).containsOnly(workflowInstanceBack);
        assertThat(workflowInstanceBack.getWorkflow()).isEqualTo(workflow);

        workflow.setWorkflowInstances(new HashSet<>());
        assertThat(workflow.getWorkflowInstances()).doesNotContain(workflowInstanceBack);
        assertThat(workflowInstanceBack.getWorkflow()).isNull();
    }
}
