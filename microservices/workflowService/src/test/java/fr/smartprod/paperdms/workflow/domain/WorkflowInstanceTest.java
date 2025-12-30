package fr.smartprod.paperdms.workflow.domain;

import static fr.smartprod.paperdms.workflow.domain.WorkflowApprovalHistoryTestSamples.*;
import static fr.smartprod.paperdms.workflow.domain.WorkflowInstanceTestSamples.*;
import static fr.smartprod.paperdms.workflow.domain.WorkflowTaskTestSamples.*;
import static fr.smartprod.paperdms.workflow.domain.WorkflowTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.workflow.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
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
    void approvalHistoriesTest() {
        WorkflowInstance workflowInstance = getWorkflowInstanceRandomSampleGenerator();
        WorkflowApprovalHistory workflowApprovalHistoryBack = getWorkflowApprovalHistoryRandomSampleGenerator();

        workflowInstance.addApprovalHistories(workflowApprovalHistoryBack);
        assertThat(workflowInstance.getApprovalHistories()).containsOnly(workflowApprovalHistoryBack);
        assertThat(workflowApprovalHistoryBack.getWorkflowInstance()).isEqualTo(workflowInstance);

        workflowInstance.removeApprovalHistories(workflowApprovalHistoryBack);
        assertThat(workflowInstance.getApprovalHistories()).doesNotContain(workflowApprovalHistoryBack);
        assertThat(workflowApprovalHistoryBack.getWorkflowInstance()).isNull();

        workflowInstance.approvalHistories(new HashSet<>(Set.of(workflowApprovalHistoryBack)));
        assertThat(workflowInstance.getApprovalHistories()).containsOnly(workflowApprovalHistoryBack);
        assertThat(workflowApprovalHistoryBack.getWorkflowInstance()).isEqualTo(workflowInstance);

        workflowInstance.setApprovalHistories(new HashSet<>());
        assertThat(workflowInstance.getApprovalHistories()).doesNotContain(workflowApprovalHistoryBack);
        assertThat(workflowApprovalHistoryBack.getWorkflowInstance()).isNull();
    }

    @Test
    void workflowTasksTest() {
        WorkflowInstance workflowInstance = getWorkflowInstanceRandomSampleGenerator();
        WorkflowTask workflowTaskBack = getWorkflowTaskRandomSampleGenerator();

        workflowInstance.addWorkflowTasks(workflowTaskBack);
        assertThat(workflowInstance.getWorkflowTasks()).containsOnly(workflowTaskBack);
        assertThat(workflowTaskBack.getInstance()).isEqualTo(workflowInstance);

        workflowInstance.removeWorkflowTasks(workflowTaskBack);
        assertThat(workflowInstance.getWorkflowTasks()).doesNotContain(workflowTaskBack);
        assertThat(workflowTaskBack.getInstance()).isNull();

        workflowInstance.workflowTasks(new HashSet<>(Set.of(workflowTaskBack)));
        assertThat(workflowInstance.getWorkflowTasks()).containsOnly(workflowTaskBack);
        assertThat(workflowTaskBack.getInstance()).isEqualTo(workflowInstance);

        workflowInstance.setWorkflowTasks(new HashSet<>());
        assertThat(workflowInstance.getWorkflowTasks()).doesNotContain(workflowTaskBack);
        assertThat(workflowTaskBack.getInstance()).isNull();
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
