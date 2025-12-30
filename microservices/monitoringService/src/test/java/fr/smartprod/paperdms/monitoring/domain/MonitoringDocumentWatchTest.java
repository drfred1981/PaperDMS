package fr.smartprod.paperdms.monitoring.domain;

import static fr.smartprod.paperdms.monitoring.domain.MonitoringDocumentWatchTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.monitoring.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MonitoringDocumentWatchTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MonitoringDocumentWatch.class);
        MonitoringDocumentWatch monitoringDocumentWatch1 = getMonitoringDocumentWatchSample1();
        MonitoringDocumentWatch monitoringDocumentWatch2 = new MonitoringDocumentWatch();
        assertThat(monitoringDocumentWatch1).isNotEqualTo(monitoringDocumentWatch2);

        monitoringDocumentWatch2.setId(monitoringDocumentWatch1.getId());
        assertThat(monitoringDocumentWatch1).isEqualTo(monitoringDocumentWatch2);

        monitoringDocumentWatch2 = getMonitoringDocumentWatchSample2();
        assertThat(monitoringDocumentWatch1).isNotEqualTo(monitoringDocumentWatch2);
    }
}
