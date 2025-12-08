package com.ged.workflow.service.mapper;

import static com.ged.workflow.domain.WorkflowStepAsserts.*;
import static com.ged.workflow.domain.WorkflowStepTestSamples.*;

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
