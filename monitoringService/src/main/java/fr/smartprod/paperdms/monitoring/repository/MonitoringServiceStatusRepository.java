package fr.smartprod.paperdms.monitoring.repository;

import fr.smartprod.paperdms.monitoring.domain.MonitoringServiceStatus;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MonitoringServiceStatus entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MonitoringServiceStatusRepository
    extends JpaRepository<MonitoringServiceStatus, Long>, JpaSpecificationExecutor<MonitoringServiceStatus> {}
