package fr.smartprod.paperdms.workflow.domain;

import static fr.smartprod.paperdms.workflow.domain.WorkflowApprovalHistoryTestSamples.*;
import static fr.smartprod.paperdms.workflow.domain.WorkflowInstanceTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.workflow.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WorkflowApprovalHistoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkflowApprovalHistory.class);
        WorkflowApprovalHistory workflowApprovalHistory1 = getWorkflowApprovalHistorySample1();
        WorkflowApprovalHistory workflowApprovalHistory2 = new WorkflowApprovalHistory();
        assertThat(workflowApprovalHistory1).isNotEqualTo(workflowApprovalHistory2);

        workflowApprovalHistory2.setId(workflowApprovalHistory1.getId());
        assertThat(workflowApprovalHistory1).isEqualTo(workflowApprovalHistory2);

        workflowApprovalHistory2 = getWorkflowApprovalHistorySample2();
        assertThat(workflowApprovalHistory1).isNotEqualTo(workflowApprovalHistory2);
    }

    @Test
    void workflowInstanceTest() {
        WorkflowApprovalHistory workflowApprovalHistory = getWorkflowApprovalHistoryRandomSampleGenerator();
        WorkflowInstance workflowInstanceBack = getWorkflowInstanceRandomSampleGenerator();

        workflowApprovalHistory.setWorkflowInstance(workflowInstanceBack);
        assertThat(workflowApprovalHistory.getWorkflowInstance()).isEqualTo(workflowInstanceBack);

        workflowApprovalHistory.workflowInstance(null);
        assertThat(workflowApprovalHistory.getWorkflowInstance()).isNull();
    }
}
