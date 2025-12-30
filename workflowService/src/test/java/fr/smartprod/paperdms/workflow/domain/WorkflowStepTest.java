package fr.smartprod.paperdms.workflow.domain;

import static fr.smartprod.paperdms.workflow.domain.WorkflowStepTestSamples.*;
import static fr.smartprod.paperdms.workflow.domain.WorkflowTaskTestSamples.*;
import static fr.smartprod.paperdms.workflow.domain.WorkflowTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.workflow.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
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
    void workflowTasksTest() {
        WorkflowStep workflowStep = getWorkflowStepRandomSampleGenerator();
        WorkflowTask workflowTaskBack = getWorkflowTaskRandomSampleGenerator();

        workflowStep.addWorkflowTasks(workflowTaskBack);
        assertThat(workflowStep.getWorkflowTasks()).containsOnly(workflowTaskBack);
        assertThat(workflowTaskBack.getStep()).isEqualTo(workflowStep);

        workflowStep.removeWorkflowTasks(workflowTaskBack);
        assertThat(workflowStep.getWorkflowTasks()).doesNotContain(workflowTaskBack);
        assertThat(workflowTaskBack.getStep()).isNull();

        workflowStep.workflowTasks(new HashSet<>(Set.of(workflowTaskBack)));
        assertThat(workflowStep.getWorkflowTasks()).containsOnly(workflowTaskBack);
        assertThat(workflowTaskBack.getStep()).isEqualTo(workflowStep);

        workflowStep.setWorkflowTasks(new HashSet<>());
        assertThat(workflowStep.getWorkflowTasks()).doesNotContain(workflowTaskBack);
        assertThat(workflowTaskBack.getStep()).isNull();
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
