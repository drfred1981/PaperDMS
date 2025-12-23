package fr.smartprod.paperdms.reporting.repository;

import fr.smartprod.paperdms.reporting.domain.DashboardWidget;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DashboardWidget entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DashboardWidgetRepository extends JpaRepository<DashboardWidget, Long> {}
