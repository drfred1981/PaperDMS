package fr.smartprod.paperdms.reporting.repository;

import fr.smartprod.paperdms.reporting.domain.PerformanceMetric;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PerformanceMetric entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PerformanceMetricRepository extends JpaRepository<PerformanceMetric, Long> {}
