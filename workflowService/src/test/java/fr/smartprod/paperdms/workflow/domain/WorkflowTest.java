package fr.smartprod.paperdms.workflow.domain;

import static fr.smartprod.paperdms.workflow.domain.WorkflowTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.workflow.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WorkflowTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Workflow.class);
        Workflow workflow1 = getWorkflowSample1();
        Workflow workflow2 = new Workflow();
        assertThat(workflow1).isNotEqualTo(workflow2);

        workflow2.setId(workflow1.getId());
        assertThat(workflow1).isEqualTo(workflow2);

        workflow2 = getWorkflowSample2();
        assertThat(workflow1).isNotEqualTo(workflow2);
    }
}
