package fr.smartprod.paperdms.monitoring.domain;

import static fr.smartprod.paperdms.monitoring.domain.MonitoringServiceStatusTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.monitoring.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MonitoringServiceStatusTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MonitoringServiceStatus.class);
        MonitoringServiceStatus monitoringServiceStatus1 = getMonitoringServiceStatusSample1();
        MonitoringServiceStatus monitoringServiceStatus2 = new MonitoringServiceStatus();
        assertThat(monitoringServiceStatus1).isNotEqualTo(monitoringServiceStatus2);

        monitoringServiceStatus2.setId(monitoringServiceStatus1.getId());
        assertThat(monitoringServiceStatus1).isEqualTo(monitoringServiceStatus2);

        monitoringServiceStatus2 = getMonitoringServiceStatusSample2();
        assertThat(monitoringServiceStatus1).isNotEqualTo(monitoringServiceStatus2);
    }
}
