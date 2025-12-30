package fr.smartprod.paperdms.transform.repository;

import fr.smartprod.paperdms.transform.domain.TransformConversionJob;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TransformConversionJob entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TransformConversionJobRepository
    extends JpaRepository<TransformConversionJob, Long>, JpaSpecificationExecutor<TransformConversionJob> {}
