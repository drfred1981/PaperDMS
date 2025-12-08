package com.ged.similarity.service;

import com.ged.similarity.service.dto.SimilarityJobDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.ged.similarity.domain.SimilarityJob}.
 */
public interface SimilarityJobService {
    /**
     * Save a similarityJob.
     *
     * @param similarityJobDTO the entity to save.
     * @return the persisted entity.
     */
    SimilarityJobDTO save(SimilarityJobDTO similarityJobDTO);

    /**
     * Updates a similarityJob.
     *
     * @param similarityJobDTO the entity to update.
     * @return the persisted entity.
     */
    SimilarityJobDTO update(SimilarityJobDTO similarityJobDTO);

    /**
     * Partially updates a similarityJob.
     *
     * @param similarityJobDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SimilarityJobDTO> partialUpdate(SimilarityJobDTO similarityJobDTO);

    /**
     * Get the "id" similarityJob.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SimilarityJobDTO> findOne(Long id);

    /**
     * Delete the "id" similarityJob.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
