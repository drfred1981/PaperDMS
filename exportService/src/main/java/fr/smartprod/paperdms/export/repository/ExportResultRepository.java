package fr.smartprod.paperdms.export.repository;

import fr.smartprod.paperdms.export.domain.ExportResult;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ExportResult entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExportResultRepository extends JpaRepository<ExportResult, Long> {}
