package fr.smartprod.paperdms.workflow.service.mapper;

import static fr.smartprod.paperdms.workflow.domain.WorkflowAsserts.*;
import static fr.smartprod.paperdms.workflow.domain.WorkflowTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WorkflowMapperTest {

    private WorkflowMapper workflowMapper;

    @BeforeEach
    void setUp() {
        workflowMapper = new WorkflowMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getWorkflowSample1();
        var actual = workflowMapper.toEntity(workflowMapper.toDto(expected));
        assertWorkflowAllPropertiesEquals(expected, actual);
    }
}
