package fr.smartprod.paperdms.reporting.repository;

import fr.smartprod.paperdms.reporting.domain.ReportingDashboard;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ReportingDashboard entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReportingDashboardRepository
    extends JpaRepository<ReportingDashboard, Long>, JpaSpecificationExecutor<ReportingDashboard> {}
