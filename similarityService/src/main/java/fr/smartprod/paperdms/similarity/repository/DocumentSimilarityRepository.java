package fr.smartprod.paperdms.similarity.repository;

import fr.smartprod.paperdms.similarity.domain.DocumentSimilarity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DocumentSimilarity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocumentSimilarityRepository
    extends JpaRepository<DocumentSimilarity, Long>, JpaSpecificationExecutor<DocumentSimilarity> {}
