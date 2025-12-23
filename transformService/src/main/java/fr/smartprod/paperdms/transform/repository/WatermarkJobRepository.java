package fr.smartprod.paperdms.transform.repository;

import fr.smartprod.paperdms.transform.domain.WatermarkJob;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the WatermarkJob entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WatermarkJobRepository extends JpaRepository<WatermarkJob, Long>, JpaSpecificationExecutor<WatermarkJob> {}
