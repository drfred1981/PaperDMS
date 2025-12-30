package fr.smartprod.paperdms.workflow.service.mapper;

import static fr.smartprod.paperdms.workflow.domain.WorkflowApprovalHistoryAsserts.*;
import static fr.smartprod.paperdms.workflow.domain.WorkflowApprovalHistoryTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WorkflowApprovalHistoryMapperTest {

    private WorkflowApprovalHistoryMapper workflowApprovalHistoryMapper;

    @BeforeEach
    void setUp() {
        workflowApprovalHistoryMapper = new WorkflowApprovalHistoryMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getWorkflowApprovalHistorySample1();
        var actual = workflowApprovalHistoryMapper.toEntity(workflowApprovalHistoryMapper.toDto(expected));
        assertWorkflowApprovalHistoryAllPropertiesEquals(expected, actual);
    }
}
