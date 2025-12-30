package fr.smartprod.paperdms.monitoring.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.monitoring.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MonitoringMaintenanceTaskDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MonitoringMaintenanceTaskDTO.class);
        MonitoringMaintenanceTaskDTO monitoringMaintenanceTaskDTO1 = new MonitoringMaintenanceTaskDTO();
        monitoringMaintenanceTaskDTO1.setId(1L);
        MonitoringMaintenanceTaskDTO monitoringMaintenanceTaskDTO2 = new MonitoringMaintenanceTaskDTO();
        assertThat(monitoringMaintenanceTaskDTO1).isNotEqualTo(monitoringMaintenanceTaskDTO2);
        monitoringMaintenanceTaskDTO2.setId(monitoringMaintenanceTaskDTO1.getId());
        assertThat(monitoringMaintenanceTaskDTO1).isEqualTo(monitoringMaintenanceTaskDTO2);
        monitoringMaintenanceTaskDTO2.setId(2L);
        assertThat(monitoringMaintenanceTaskDTO1).isNotEqualTo(monitoringMaintenanceTaskDTO2);
        monitoringMaintenanceTaskDTO1.setId(null);
        assertThat(monitoringMaintenanceTaskDTO1).isNotEqualTo(monitoringMaintenanceTaskDTO2);
    }
}
