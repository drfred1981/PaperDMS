package com.ged.similarity.repository;

import com.ged.similarity.domain.SimilarityJob;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SimilarityJob entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SimilarityJobRepository extends JpaRepository<SimilarityJob, Long>, JpaSpecificationExecutor<SimilarityJob> {}
