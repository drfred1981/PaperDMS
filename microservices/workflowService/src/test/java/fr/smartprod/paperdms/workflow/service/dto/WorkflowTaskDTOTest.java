package fr.smartprod.paperdms.workflow.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.workflow.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WorkflowTaskDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkflowTaskDTO.class);
        WorkflowTaskDTO workflowTaskDTO1 = new WorkflowTaskDTO();
        workflowTaskDTO1.setId(1L);
        WorkflowTaskDTO workflowTaskDTO2 = new WorkflowTaskDTO();
        assertThat(workflowTaskDTO1).isNotEqualTo(workflowTaskDTO2);
        workflowTaskDTO2.setId(workflowTaskDTO1.getId());
        assertThat(workflowTaskDTO1).isEqualTo(workflowTaskDTO2);
        workflowTaskDTO2.setId(2L);
        assertThat(workflowTaskDTO1).isNotEqualTo(workflowTaskDTO2);
        workflowTaskDTO1.setId(null);
        assertThat(workflowTaskDTO1).isNotEqualTo(workflowTaskDTO2);
    }
}
