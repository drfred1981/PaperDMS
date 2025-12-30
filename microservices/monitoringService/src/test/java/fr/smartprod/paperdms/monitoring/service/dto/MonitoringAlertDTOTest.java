package fr.smartprod.paperdms.monitoring.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.monitoring.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MonitoringAlertDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MonitoringAlertDTO.class);
        MonitoringAlertDTO monitoringAlertDTO1 = new MonitoringAlertDTO();
        monitoringAlertDTO1.setId(1L);
        MonitoringAlertDTO monitoringAlertDTO2 = new MonitoringAlertDTO();
        assertThat(monitoringAlertDTO1).isNotEqualTo(monitoringAlertDTO2);
        monitoringAlertDTO2.setId(monitoringAlertDTO1.getId());
        assertThat(monitoringAlertDTO1).isEqualTo(monitoringAlertDTO2);
        monitoringAlertDTO2.setId(2L);
        assertThat(monitoringAlertDTO1).isNotEqualTo(monitoringAlertDTO2);
        monitoringAlertDTO1.setId(null);
        assertThat(monitoringAlertDTO1).isNotEqualTo(monitoringAlertDTO2);
    }
}
