package fr.smartprod.paperdms.workflow.domain;

import static fr.smartprod.paperdms.workflow.domain.ApprovalHistoryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.workflow.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ApprovalHistoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApprovalHistory.class);
        ApprovalHistory approvalHistory1 = getApprovalHistorySample1();
        ApprovalHistory approvalHistory2 = new ApprovalHistory();
        assertThat(approvalHistory1).isNotEqualTo(approvalHistory2);

        approvalHistory2.setId(approvalHistory1.getId());
        assertThat(approvalHistory1).isEqualTo(approvalHistory2);

        approvalHistory2 = getApprovalHistorySample2();
        assertThat(approvalHistory1).isNotEqualTo(approvalHistory2);
    }
}
