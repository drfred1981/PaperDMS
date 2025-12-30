package fr.smartprod.paperdms.reporting.repository;

import fr.smartprod.paperdms.reporting.domain.ReportingExecution;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ReportingExecution entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReportingExecutionRepository
    extends JpaRepository<ReportingExecution, Long>, JpaSpecificationExecutor<ReportingExecution> {}
