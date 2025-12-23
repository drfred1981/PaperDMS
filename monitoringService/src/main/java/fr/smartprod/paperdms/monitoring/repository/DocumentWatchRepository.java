package fr.smartprod.paperdms.monitoring.repository;

import fr.smartprod.paperdms.monitoring.domain.DocumentWatch;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DocumentWatch entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocumentWatchRepository extends JpaRepository<DocumentWatch, Long>, JpaSpecificationExecutor<DocumentWatch> {}
