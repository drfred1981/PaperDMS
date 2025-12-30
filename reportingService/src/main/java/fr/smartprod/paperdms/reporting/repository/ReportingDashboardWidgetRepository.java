package fr.smartprod.paperdms.reporting.repository;

import fr.smartprod.paperdms.reporting.domain.ReportingDashboardWidget;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ReportingDashboardWidget entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReportingDashboardWidgetRepository
    extends JpaRepository<ReportingDashboardWidget, Long>, JpaSpecificationExecutor<ReportingDashboardWidget> {}
