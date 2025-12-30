package fr.smartprod.paperdms.transform.repository;

import fr.smartprod.paperdms.transform.domain.TransformMergeJob;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TransformMergeJob entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TransformMergeJobRepository extends JpaRepository<TransformMergeJob, Long>, JpaSpecificationExecutor<TransformMergeJob> {}
