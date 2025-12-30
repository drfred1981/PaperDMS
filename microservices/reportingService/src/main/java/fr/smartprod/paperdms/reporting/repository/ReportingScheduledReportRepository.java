package fr.smartprod.paperdms.reporting.repository;

import fr.smartprod.paperdms.reporting.domain.ReportingScheduledReport;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ReportingScheduledReport entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReportingScheduledReportRepository
    extends JpaRepository<ReportingScheduledReport, Long>, JpaSpecificationExecutor<ReportingScheduledReport> {}
