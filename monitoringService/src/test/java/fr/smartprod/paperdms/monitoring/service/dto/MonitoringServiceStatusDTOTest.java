package fr.smartprod.paperdms.monitoring.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.monitoring.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MonitoringServiceStatusDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MonitoringServiceStatusDTO.class);
        MonitoringServiceStatusDTO monitoringServiceStatusDTO1 = new MonitoringServiceStatusDTO();
        monitoringServiceStatusDTO1.setId(1L);
        MonitoringServiceStatusDTO monitoringServiceStatusDTO2 = new MonitoringServiceStatusDTO();
        assertThat(monitoringServiceStatusDTO1).isNotEqualTo(monitoringServiceStatusDTO2);
        monitoringServiceStatusDTO2.setId(monitoringServiceStatusDTO1.getId());
        assertThat(monitoringServiceStatusDTO1).isEqualTo(monitoringServiceStatusDTO2);
        monitoringServiceStatusDTO2.setId(2L);
        assertThat(monitoringServiceStatusDTO1).isNotEqualTo(monitoringServiceStatusDTO2);
        monitoringServiceStatusDTO1.setId(null);
        assertThat(monitoringServiceStatusDTO1).isNotEqualTo(monitoringServiceStatusDTO2);
    }
}
