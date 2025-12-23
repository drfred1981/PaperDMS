package fr.smartprod.paperdms.workflow.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.workflow.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WorkflowInstanceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkflowInstanceDTO.class);
        WorkflowInstanceDTO workflowInstanceDTO1 = new WorkflowInstanceDTO();
        workflowInstanceDTO1.setId(1L);
        WorkflowInstanceDTO workflowInstanceDTO2 = new WorkflowInstanceDTO();
        assertThat(workflowInstanceDTO1).isNotEqualTo(workflowInstanceDTO2);
        workflowInstanceDTO2.setId(workflowInstanceDTO1.getId());
        assertThat(workflowInstanceDTO1).isEqualTo(workflowInstanceDTO2);
        workflowInstanceDTO2.setId(2L);
        assertThat(workflowInstanceDTO1).isNotEqualTo(workflowInstanceDTO2);
        workflowInstanceDTO1.setId(null);
        assertThat(workflowInstanceDTO1).isNotEqualTo(workflowInstanceDTO2);
    }
}
