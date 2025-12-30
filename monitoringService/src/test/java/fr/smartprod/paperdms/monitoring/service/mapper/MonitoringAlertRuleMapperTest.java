package fr.smartprod.paperdms.monitoring.service.mapper;

import static fr.smartprod.paperdms.monitoring.domain.MonitoringAlertRuleAsserts.*;
import static fr.smartprod.paperdms.monitoring.domain.MonitoringAlertRuleTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MonitoringAlertRuleMapperTest {

    private MonitoringAlertRuleMapper monitoringAlertRuleMapper;

    @BeforeEach
    void setUp() {
        monitoringAlertRuleMapper = new MonitoringAlertRuleMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMonitoringAlertRuleSample1();
        var actual = monitoringAlertRuleMapper.toEntity(monitoringAlertRuleMapper.toDto(expected));
        assertMonitoringAlertRuleAllPropertiesEquals(expected, actual);
    }
}
