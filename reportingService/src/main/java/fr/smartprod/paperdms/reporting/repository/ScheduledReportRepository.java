package fr.smartprod.paperdms.reporting.repository;

import fr.smartprod.paperdms.reporting.domain.ScheduledReport;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ScheduledReport entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ScheduledReportRepository extends JpaRepository<ScheduledReport, Long>, JpaSpecificationExecutor<ScheduledReport> {}
