package fr.smartprod.paperdms.monitoring.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.monitoring.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MonitoringDocumentWatchDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MonitoringDocumentWatchDTO.class);
        MonitoringDocumentWatchDTO monitoringDocumentWatchDTO1 = new MonitoringDocumentWatchDTO();
        monitoringDocumentWatchDTO1.setId(1L);
        MonitoringDocumentWatchDTO monitoringDocumentWatchDTO2 = new MonitoringDocumentWatchDTO();
        assertThat(monitoringDocumentWatchDTO1).isNotEqualTo(monitoringDocumentWatchDTO2);
        monitoringDocumentWatchDTO2.setId(monitoringDocumentWatchDTO1.getId());
        assertThat(monitoringDocumentWatchDTO1).isEqualTo(monitoringDocumentWatchDTO2);
        monitoringDocumentWatchDTO2.setId(2L);
        assertThat(monitoringDocumentWatchDTO1).isNotEqualTo(monitoringDocumentWatchDTO2);
        monitoringDocumentWatchDTO1.setId(null);
        assertThat(monitoringDocumentWatchDTO1).isNotEqualTo(monitoringDocumentWatchDTO2);
    }
}
