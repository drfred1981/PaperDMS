package fr.smartprod.paperdms.workflow.domain;

import static fr.smartprod.paperdms.workflow.domain.WorkflowInstanceTestSamples.*;
import static fr.smartprod.paperdms.workflow.domain.WorkflowTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.workflow.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WorkflowInstanceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkflowInstance.class);
        WorkflowInstance workflowInstance1 = getWorkflowInstanceSample1();
        WorkflowInstance workflowInstance2 = new WorkflowInstance();
        assertThat(workflowInstance1).isNotEqualTo(workflowInstance2);

        workflowInstance2.setId(workflowInstance1.getId());
        assertThat(workflowInstance1).isEqualTo(workflowInstance2);

        workflowInstance2 = getWorkflowInstanceSample2();
        assertThat(workflowInstance1).isNotEqualTo(workflowInstance2);
    }

    @Test
    void workflowTest() {
        WorkflowInstance workflowInstance = getWorkflowInstanceRandomSampleGenerator();
        Workflow workflowBack = getWorkflowRandomSampleGenerator();

        workflowInstance.setWorkflow(workflowBack);
        assertThat(workflowInstance.getWorkflow()).isEqualTo(workflowBack);

        workflowInstance.workflow(null);
        assertThat(workflowInstance.getWorkflow()).isNull();
    }
}
