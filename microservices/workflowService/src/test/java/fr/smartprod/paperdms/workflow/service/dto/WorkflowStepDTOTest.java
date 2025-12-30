package fr.smartprod.paperdms.workflow.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.workflow.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WorkflowStepDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkflowStepDTO.class);
        WorkflowStepDTO workflowStepDTO1 = new WorkflowStepDTO();
        workflowStepDTO1.setId(1L);
        WorkflowStepDTO workflowStepDTO2 = new WorkflowStepDTO();
        assertThat(workflowStepDTO1).isNotEqualTo(workflowStepDTO2);
        workflowStepDTO2.setId(workflowStepDTO1.getId());
        assertThat(workflowStepDTO1).isEqualTo(workflowStepDTO2);
        workflowStepDTO2.setId(2L);
        assertThat(workflowStepDTO1).isNotEqualTo(workflowStepDTO2);
        workflowStepDTO1.setId(null);
        assertThat(workflowStepDTO1).isNotEqualTo(workflowStepDTO2);
    }
}
