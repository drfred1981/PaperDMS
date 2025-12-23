package fr.smartprod.paperdms.scan.repository;

import fr.smartprod.paperdms.scan.domain.ScanBatch;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ScanBatch entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ScanBatchRepository extends JpaRepository<ScanBatch, Long>, JpaSpecificationExecutor<ScanBatch> {}
