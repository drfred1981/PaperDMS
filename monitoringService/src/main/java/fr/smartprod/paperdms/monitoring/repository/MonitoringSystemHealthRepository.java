package fr.smartprod.paperdms.monitoring.repository;

import fr.smartprod.paperdms.monitoring.domain.MonitoringSystemHealth;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MonitoringSystemHealth entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MonitoringSystemHealthRepository
    extends JpaRepository<MonitoringSystemHealth, Long>, JpaSpecificationExecutor<MonitoringSystemHealth> {}
