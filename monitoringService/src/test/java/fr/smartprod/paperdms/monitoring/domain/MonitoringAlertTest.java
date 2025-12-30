package fr.smartprod.paperdms.monitoring.domain;

import static fr.smartprod.paperdms.monitoring.domain.MonitoringAlertRuleTestSamples.*;
import static fr.smartprod.paperdms.monitoring.domain.MonitoringAlertTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.monitoring.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MonitoringAlertTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MonitoringAlert.class);
        MonitoringAlert monitoringAlert1 = getMonitoringAlertSample1();
        MonitoringAlert monitoringAlert2 = new MonitoringAlert();
        assertThat(monitoringAlert1).isNotEqualTo(monitoringAlert2);

        monitoringAlert2.setId(monitoringAlert1.getId());
        assertThat(monitoringAlert1).isEqualTo(monitoringAlert2);

        monitoringAlert2 = getMonitoringAlertSample2();
        assertThat(monitoringAlert1).isNotEqualTo(monitoringAlert2);
    }

    @Test
    void alertRuleTest() {
        MonitoringAlert monitoringAlert = getMonitoringAlertRandomSampleGenerator();
        MonitoringAlertRule monitoringAlertRuleBack = getMonitoringAlertRuleRandomSampleGenerator();

        monitoringAlert.setAlertRule(monitoringAlertRuleBack);
        assertThat(monitoringAlert.getAlertRule()).isEqualTo(monitoringAlertRuleBack);

        monitoringAlert.alertRule(null);
        assertThat(monitoringAlert.getAlertRule()).isNull();
    }
}
