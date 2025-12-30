package fr.smartprod.paperdms.monitoring.domain;

import static fr.smartprod.paperdms.monitoring.domain.MonitoringMaintenanceTaskTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.monitoring.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MonitoringMaintenanceTaskTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MonitoringMaintenanceTask.class);
        MonitoringMaintenanceTask monitoringMaintenanceTask1 = getMonitoringMaintenanceTaskSample1();
        MonitoringMaintenanceTask monitoringMaintenanceTask2 = new MonitoringMaintenanceTask();
        assertThat(monitoringMaintenanceTask1).isNotEqualTo(monitoringMaintenanceTask2);

        monitoringMaintenanceTask2.setId(monitoringMaintenanceTask1.getId());
        assertThat(monitoringMaintenanceTask1).isEqualTo(monitoringMaintenanceTask2);

        monitoringMaintenanceTask2 = getMonitoringMaintenanceTaskSample2();
        assertThat(monitoringMaintenanceTask1).isNotEqualTo(monitoringMaintenanceTask2);
    }
}
