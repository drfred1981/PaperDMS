package fr.smartprod.paperdms.export.repository;

import fr.smartprod.paperdms.export.domain.ExportJob;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ExportJob entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExportJobRepository extends JpaRepository<ExportJob, Long>, JpaSpecificationExecutor<ExportJob> {}
