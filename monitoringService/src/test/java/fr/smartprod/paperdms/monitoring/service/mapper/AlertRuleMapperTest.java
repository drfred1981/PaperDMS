package fr.smartprod.paperdms.monitoring.service.mapper;

import static fr.smartprod.paperdms.monitoring.domain.AlertRuleAsserts.*;
import static fr.smartprod.paperdms.monitoring.domain.AlertRuleTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlertRuleMapperTest {

    private AlertRuleMapper alertRuleMapper;

    @BeforeEach
    void setUp() {
        alertRuleMapper = new AlertRuleMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAlertRuleSample1();
        var actual = alertRuleMapper.toEntity(alertRuleMapper.toDto(expected));
        assertAlertRuleAllPropertiesEquals(expected, actual);
    }
}
