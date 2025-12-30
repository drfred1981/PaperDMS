package fr.smartprod.paperdms.monitoring.repository;

import fr.smartprod.paperdms.monitoring.domain.MonitoringMaintenanceTask;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MonitoringMaintenanceTask entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MonitoringMaintenanceTaskRepository
    extends JpaRepository<MonitoringMaintenanceTask, Long>, JpaSpecificationExecutor<MonitoringMaintenanceTask> {}
