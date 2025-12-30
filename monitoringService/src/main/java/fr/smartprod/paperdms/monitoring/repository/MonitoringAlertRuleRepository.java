package fr.smartprod.paperdms.monitoring.repository;

import fr.smartprod.paperdms.monitoring.domain.MonitoringAlertRule;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MonitoringAlertRule entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MonitoringAlertRuleRepository
    extends JpaRepository<MonitoringAlertRule, Long>, JpaSpecificationExecutor<MonitoringAlertRule> {}
