package fr.smartprod.paperdms.similarity.repository;

import fr.smartprod.paperdms.similarity.domain.SimilarityCluster;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SimilarityCluster entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SimilarityClusterRepository extends JpaRepository<SimilarityCluster, Long> {}
