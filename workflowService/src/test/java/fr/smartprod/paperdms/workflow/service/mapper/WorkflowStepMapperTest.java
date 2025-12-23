package fr.smartprod.paperdms.workflow.service.mapper;

import static fr.smartprod.paperdms.workflow.domain.WorkflowStepAsserts.*;
import static fr.smartprod.paperdms.workflow.domain.WorkflowStepTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WorkflowStepMapperTest {

    private WorkflowStepMapper workflowStepMapper;

    @BeforeEach
    void setUp() {
        workflowStepMapper = new WorkflowStepMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getWorkflowStepSample1();
        var actual = workflowStepMapper.toEntity(workflowStepMapper.toDto(expected));
        assertWorkflowStepAllPropertiesEquals(expected, actual);
    }
}
