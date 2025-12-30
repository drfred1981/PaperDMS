package fr.smartprod.paperdms.similarity.repository;

import fr.smartprod.paperdms.similarity.domain.SimilarityJob;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SimilarityJob entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SimilarityJobRepository extends JpaRepository<SimilarityJob, Long>, JpaSpecificationExecutor<SimilarityJob> {}
