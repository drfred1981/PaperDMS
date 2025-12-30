package fr.smartprod.paperdms.transform.repository;

import fr.smartprod.paperdms.transform.domain.TransformWatermarkJob;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TransformWatermarkJob entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TransformWatermarkJobRepository
    extends JpaRepository<TransformWatermarkJob, Long>, JpaSpecificationExecutor<TransformWatermarkJob> {}
