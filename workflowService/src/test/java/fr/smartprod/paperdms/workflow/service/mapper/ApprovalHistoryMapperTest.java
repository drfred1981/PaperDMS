package fr.smartprod.paperdms.workflow.service.mapper;

import static fr.smartprod.paperdms.workflow.domain.ApprovalHistoryAsserts.*;
import static fr.smartprod.paperdms.workflow.domain.ApprovalHistoryTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ApprovalHistoryMapperTest {

    private ApprovalHistoryMapper approvalHistoryMapper;

    @BeforeEach
    void setUp() {
        approvalHistoryMapper = new ApprovalHistoryMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getApprovalHistorySample1();
        var actual = approvalHistoryMapper.toEntity(approvalHistoryMapper.toDto(expected));
        assertApprovalHistoryAllPropertiesEquals(expected, actual);
    }
}
