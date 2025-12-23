package fr.smartprod.paperdms.monitoring.repository;

import fr.smartprod.paperdms.monitoring.domain.AlertRule;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AlertRule entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlertRuleRepository extends JpaRepository<AlertRule, Long>, JpaSpecificationExecutor<AlertRule> {}
