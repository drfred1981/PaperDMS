package fr.smartprod.paperdms.monitoring.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.monitoring.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MaintenanceTaskDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MaintenanceTaskDTO.class);
        MaintenanceTaskDTO maintenanceTaskDTO1 = new MaintenanceTaskDTO();
        maintenanceTaskDTO1.setId(1L);
        MaintenanceTaskDTO maintenanceTaskDTO2 = new MaintenanceTaskDTO();
        assertThat(maintenanceTaskDTO1).isNotEqualTo(maintenanceTaskDTO2);
        maintenanceTaskDTO2.setId(maintenanceTaskDTO1.getId());
        assertThat(maintenanceTaskDTO1).isEqualTo(maintenanceTaskDTO2);
        maintenanceTaskDTO2.setId(2L);
        assertThat(maintenanceTaskDTO1).isNotEqualTo(maintenanceTaskDTO2);
        maintenanceTaskDTO1.setId(null);
        assertThat(maintenanceTaskDTO1).isNotEqualTo(maintenanceTaskDTO2);
    }
}
