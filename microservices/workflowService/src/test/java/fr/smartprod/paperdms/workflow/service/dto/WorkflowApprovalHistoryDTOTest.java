package fr.smartprod.paperdms.workflow.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.workflow.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WorkflowApprovalHistoryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkflowApprovalHistoryDTO.class);
        WorkflowApprovalHistoryDTO workflowApprovalHistoryDTO1 = new WorkflowApprovalHistoryDTO();
        workflowApprovalHistoryDTO1.setId(1L);
        WorkflowApprovalHistoryDTO workflowApprovalHistoryDTO2 = new WorkflowApprovalHistoryDTO();
        assertThat(workflowApprovalHistoryDTO1).isNotEqualTo(workflowApprovalHistoryDTO2);
        workflowApprovalHistoryDTO2.setId(workflowApprovalHistoryDTO1.getId());
        assertThat(workflowApprovalHistoryDTO1).isEqualTo(workflowApprovalHistoryDTO2);
        workflowApprovalHistoryDTO2.setId(2L);
        assertThat(workflowApprovalHistoryDTO1).isNotEqualTo(workflowApprovalHistoryDTO2);
        workflowApprovalHistoryDTO1.setId(null);
        assertThat(workflowApprovalHistoryDTO1).isNotEqualTo(workflowApprovalHistoryDTO2);
    }
}
