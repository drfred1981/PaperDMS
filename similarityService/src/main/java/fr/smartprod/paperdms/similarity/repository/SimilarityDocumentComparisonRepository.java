package fr.smartprod.paperdms.similarity.repository;

import fr.smartprod.paperdms.similarity.domain.SimilarityDocumentComparison;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SimilarityDocumentComparison entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SimilarityDocumentComparisonRepository
    extends JpaRepository<SimilarityDocumentComparison, Long>, JpaSpecificationExecutor<SimilarityDocumentComparison> {}
