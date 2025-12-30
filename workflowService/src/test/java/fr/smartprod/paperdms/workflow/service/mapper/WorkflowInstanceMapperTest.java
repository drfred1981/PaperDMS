package fr.smartprod.paperdms.workflow.service.mapper;

import static fr.smartprod.paperdms.workflow.domain.WorkflowInstanceAsserts.*;
import static fr.smartprod.paperdms.workflow.domain.WorkflowInstanceTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WorkflowInstanceMapperTest {

    private WorkflowInstanceMapper workflowInstanceMapper;

    @BeforeEach
    void setUp() {
        workflowInstanceMapper = new WorkflowInstanceMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getWorkflowInstanceSample1();
        var actual = workflowInstanceMapper.toEntity(workflowInstanceMapper.toDto(expected));
        assertWorkflowInstanceAllPropertiesEquals(expected, actual);
    }
}
