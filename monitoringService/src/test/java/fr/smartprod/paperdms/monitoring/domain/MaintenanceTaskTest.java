package fr.smartprod.paperdms.monitoring.domain;

import static fr.smartprod.paperdms.monitoring.domain.MaintenanceTaskTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.monitoring.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MaintenanceTaskTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MaintenanceTask.class);
        MaintenanceTask maintenanceTask1 = getMaintenanceTaskSample1();
        MaintenanceTask maintenanceTask2 = new MaintenanceTask();
        assertThat(maintenanceTask1).isNotEqualTo(maintenanceTask2);

        maintenanceTask2.setId(maintenanceTask1.getId());
        assertThat(maintenanceTask1).isEqualTo(maintenanceTask2);

        maintenanceTask2 = getMaintenanceTaskSample2();
        assertThat(maintenanceTask1).isNotEqualTo(maintenanceTask2);
    }
}
