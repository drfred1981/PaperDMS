package fr.smartprod.paperdms.workflow.service.mapper;

import static fr.smartprod.paperdms.workflow.domain.WorkflowTaskAsserts.*;
import static fr.smartprod.paperdms.workflow.domain.WorkflowTaskTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WorkflowTaskMapperTest {

    private WorkflowTaskMapper workflowTaskMapper;

    @BeforeEach
    void setUp() {
        workflowTaskMapper = new WorkflowTaskMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getWorkflowTaskSample1();
        var actual = workflowTaskMapper.toEntity(workflowTaskMapper.toDto(expected));
        assertWorkflowTaskAllPropertiesEquals(expected, actual);
    }
}
