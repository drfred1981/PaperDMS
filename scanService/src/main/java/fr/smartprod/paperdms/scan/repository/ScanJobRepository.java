package fr.smartprod.paperdms.scan.repository;

import fr.smartprod.paperdms.scan.domain.ScanJob;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ScanJob entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ScanJobRepository extends JpaRepository<ScanJob, Long>, JpaSpecificationExecutor<ScanJob> {}
