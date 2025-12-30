package fr.smartprod.paperdms.monitoring.domain;

import static fr.smartprod.paperdms.monitoring.domain.MonitoringSystemHealthTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.monitoring.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MonitoringSystemHealthTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MonitoringSystemHealth.class);
        MonitoringSystemHealth monitoringSystemHealth1 = getMonitoringSystemHealthSample1();
        MonitoringSystemHealth monitoringSystemHealth2 = new MonitoringSystemHealth();
        assertThat(monitoringSystemHealth1).isNotEqualTo(monitoringSystemHealth2);

        monitoringSystemHealth2.setId(monitoringSystemHealth1.getId());
        assertThat(monitoringSystemHealth1).isEqualTo(monitoringSystemHealth2);

        monitoringSystemHealth2 = getMonitoringSystemHealthSample2();
        assertThat(monitoringSystemHealth1).isNotEqualTo(monitoringSystemHealth2);
    }
}
