package com.ged.workflow.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ged.workflow.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ApprovalHistoryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApprovalHistoryDTO.class);
        ApprovalHistoryDTO approvalHistoryDTO1 = new ApprovalHistoryDTO();
        approvalHistoryDTO1.setId(1L);
        ApprovalHistoryDTO approvalHistoryDTO2 = new ApprovalHistoryDTO();
        assertThat(approvalHistoryDTO1).isNotEqualTo(approvalHistoryDTO2);
        approvalHistoryDTO2.setId(approvalHistoryDTO1.getId());
        assertThat(approvalHistoryDTO1).isEqualTo(approvalHistoryDTO2);
        approvalHistoryDTO2.setId(2L);
        assertThat(approvalHistoryDTO1).isNotEqualTo(approvalHistoryDTO2);
        approvalHistoryDTO1.setId(null);
        assertThat(approvalHistoryDTO1).isNotEqualTo(approvalHistoryDTO2);
    }
}
