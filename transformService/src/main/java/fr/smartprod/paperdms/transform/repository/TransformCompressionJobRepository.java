package fr.smartprod.paperdms.transform.repository;

import fr.smartprod.paperdms.transform.domain.TransformCompressionJob;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TransformCompressionJob entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TransformCompressionJobRepository
    extends JpaRepository<TransformCompressionJob, Long>, JpaSpecificationExecutor<TransformCompressionJob> {}
