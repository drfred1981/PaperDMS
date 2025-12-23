package fr.smartprod.paperdms.reporting.repository;

import fr.smartprod.paperdms.reporting.domain.SystemMetric;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SystemMetric entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SystemMetricRepository extends JpaRepository<SystemMetric, Long> {}
