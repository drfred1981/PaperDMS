package fr.smartprod.paperdms.monitoring.domain;

import static fr.smartprod.paperdms.monitoring.domain.MonitoringAlertRuleTestSamples.*;
import static fr.smartprod.paperdms.monitoring.domain.MonitoringAlertTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.monitoring.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class MonitoringAlertRuleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MonitoringAlertRule.class);
        MonitoringAlertRule monitoringAlertRule1 = getMonitoringAlertRuleSample1();
        MonitoringAlertRule monitoringAlertRule2 = new MonitoringAlertRule();
        assertThat(monitoringAlertRule1).isNotEqualTo(monitoringAlertRule2);

        monitoringAlertRule2.setId(monitoringAlertRule1.getId());
        assertThat(monitoringAlertRule1).isEqualTo(monitoringAlertRule2);

        monitoringAlertRule2 = getMonitoringAlertRuleSample2();
        assertThat(monitoringAlertRule1).isNotEqualTo(monitoringAlertRule2);
    }

    @Test
    void alertsTest() {
        MonitoringAlertRule monitoringAlertRule = getMonitoringAlertRuleRandomSampleGenerator();
        MonitoringAlert monitoringAlertBack = getMonitoringAlertRandomSampleGenerator();

        monitoringAlertRule.addAlerts(monitoringAlertBack);
        assertThat(monitoringAlertRule.getAlerts()).containsOnly(monitoringAlertBack);
        assertThat(monitoringAlertBack.getAlertRule()).isEqualTo(monitoringAlertRule);

        monitoringAlertRule.removeAlerts(monitoringAlertBack);
        assertThat(monitoringAlertRule.getAlerts()).doesNotContain(monitoringAlertBack);
        assertThat(monitoringAlertBack.getAlertRule()).isNull();

        monitoringAlertRule.alerts(new HashSet<>(Set.of(monitoringAlertBack)));
        assertThat(monitoringAlertRule.getAlerts()).containsOnly(monitoringAlertBack);
        assertThat(monitoringAlertBack.getAlertRule()).isEqualTo(monitoringAlertRule);

        monitoringAlertRule.setAlerts(new HashSet<>());
        assertThat(monitoringAlertRule.getAlerts()).doesNotContain(monitoringAlertBack);
        assertThat(monitoringAlertBack.getAlertRule()).isNull();
    }
}
