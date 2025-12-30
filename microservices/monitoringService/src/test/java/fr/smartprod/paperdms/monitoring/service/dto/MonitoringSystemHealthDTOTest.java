package fr.smartprod.paperdms.monitoring.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.monitoring.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MonitoringSystemHealthDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MonitoringSystemHealthDTO.class);
        MonitoringSystemHealthDTO monitoringSystemHealthDTO1 = new MonitoringSystemHealthDTO();
        monitoringSystemHealthDTO1.setId(1L);
        MonitoringSystemHealthDTO monitoringSystemHealthDTO2 = new MonitoringSystemHealthDTO();
        assertThat(monitoringSystemHealthDTO1).isNotEqualTo(monitoringSystemHealthDTO2);
        monitoringSystemHealthDTO2.setId(monitoringSystemHealthDTO1.getId());
        assertThat(monitoringSystemHealthDTO1).isEqualTo(monitoringSystemHealthDTO2);
        monitoringSystemHealthDTO2.setId(2L);
        assertThat(monitoringSystemHealthDTO1).isNotEqualTo(monitoringSystemHealthDTO2);
        monitoringSystemHealthDTO1.setId(null);
        assertThat(monitoringSystemHealthDTO1).isNotEqualTo(monitoringSystemHealthDTO2);
    }
}
