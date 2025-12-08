package com.ged.similarity.repository;

import com.ged.similarity.domain.DocumentSimilarity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DocumentSimilarity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocumentSimilarityRepository
    extends JpaRepository<DocumentSimilarity, Long>, JpaSpecificationExecutor<DocumentSimilarity> {}
