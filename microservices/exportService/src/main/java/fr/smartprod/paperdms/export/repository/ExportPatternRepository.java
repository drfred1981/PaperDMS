package fr.smartprod.paperdms.export.repository;

import fr.smartprod.paperdms.export.domain.ExportPattern;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ExportPattern entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExportPatternRepository extends JpaRepository<ExportPattern, Long>, JpaSpecificationExecutor<ExportPattern> {}
