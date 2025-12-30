package fr.smartprod.paperdms.monitoring.repository;

import fr.smartprod.paperdms.monitoring.domain.MonitoringDocumentWatch;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MonitoringDocumentWatch entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MonitoringDocumentWatchRepository
    extends JpaRepository<MonitoringDocumentWatch, Long>, JpaSpecificationExecutor<MonitoringDocumentWatch> {}
