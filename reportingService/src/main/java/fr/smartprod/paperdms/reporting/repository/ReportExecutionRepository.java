package fr.smartprod.paperdms.reporting.repository;

import fr.smartprod.paperdms.reporting.domain.ReportExecution;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ReportExecution entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReportExecutionRepository extends JpaRepository<ReportExecution, Long> {}
