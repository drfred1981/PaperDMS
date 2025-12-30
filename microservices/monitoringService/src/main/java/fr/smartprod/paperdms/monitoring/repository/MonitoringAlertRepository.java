package fr.smartprod.paperdms.monitoring.repository;

import fr.smartprod.paperdms.monitoring.domain.MonitoringAlert;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MonitoringAlert entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MonitoringAlertRepository extends JpaRepository<MonitoringAlert, Long>, JpaSpecificationExecutor<MonitoringAlert> {}
