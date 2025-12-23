package fr.smartprod.paperdms.reporting.repository;

import fr.smartprod.paperdms.reporting.domain.Dashboard;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Dashboard entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DashboardRepository extends JpaRepository<Dashboard, Long>, JpaSpecificationExecutor<Dashboard> {}
