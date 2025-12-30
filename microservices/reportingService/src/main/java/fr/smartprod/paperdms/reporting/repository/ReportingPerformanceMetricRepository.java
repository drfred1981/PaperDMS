package fr.smartprod.paperdms.reporting.repository;

import fr.smartprod.paperdms.reporting.domain.ReportingPerformanceMetric;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ReportingPerformanceMetric entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReportingPerformanceMetricRepository
    extends JpaRepository<ReportingPerformanceMetric, Long>, JpaSpecificationExecutor<ReportingPerformanceMetric> {}
