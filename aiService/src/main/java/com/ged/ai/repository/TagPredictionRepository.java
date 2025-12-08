package com.ged.ai.repository;

import com.ged.ai.domain.TagPrediction;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TagPrediction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TagPredictionRepository extends JpaRepository<TagPrediction, Long> {}
