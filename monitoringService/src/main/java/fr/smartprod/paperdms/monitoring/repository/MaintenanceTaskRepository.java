package fr.smartprod.paperdms.monitoring.repository;

import fr.smartprod.paperdms.monitoring.domain.MaintenanceTask;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MaintenanceTask entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MaintenanceTaskRepository extends JpaRepository<MaintenanceTask, Long>, JpaSpecificationExecutor<MaintenanceTask> {}
