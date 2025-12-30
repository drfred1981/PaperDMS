package fr.smartprod.paperdms.reporting.repository;

import fr.smartprod.paperdms.reporting.domain.ReportingSystemMetric;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ReportingSystemMetric entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReportingSystemMetricRepository
    extends JpaRepository<ReportingSystemMetric, Long>, JpaSpecificationExecutor<ReportingSystemMetric> {}
