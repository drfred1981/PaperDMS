package com.ged.workflow.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ged.workflow.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WorkflowDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkflowDTO.class);
        WorkflowDTO workflowDTO1 = new WorkflowDTO();
        workflowDTO1.setId(1L);
        WorkflowDTO workflowDTO2 = new WorkflowDTO();
        assertThat(workflowDTO1).isNotEqualTo(workflowDTO2);
        workflowDTO2.setId(workflowDTO1.getId());
        assertThat(workflowDTO1).isEqualTo(workflowDTO2);
        workflowDTO2.setId(2L);
        assertThat(workflowDTO1).isNotEqualTo(workflowDTO2);
        workflowDTO1.setId(null);
        assertThat(workflowDTO1).isNotEqualTo(workflowDTO2);
    }
}
