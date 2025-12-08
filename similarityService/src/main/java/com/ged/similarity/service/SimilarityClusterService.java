package com.ged.similarity.service;

import com.ged.similarity.service.dto.SimilarityClusterDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.ged.similarity.domain.SimilarityCluster}.
 */
public interface SimilarityClusterService {
    /**
     * Save a similarityCluster.
     *
     * @param similarityClusterDTO the entity to save.
     * @return the persisted entity.
     */
    SimilarityClusterDTO save(SimilarityClusterDTO similarityClusterDTO);

    /**
     * Updates a similarityCluster.
     *
     * @param similarityClusterDTO the entity to update.
     * @return the persisted entity.
     */
    SimilarityClusterDTO update(SimilarityClusterDTO similarityClusterDTO);

    /**
     * Partially updates a similarityCluster.
     *
     * @param similarityClusterDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SimilarityClusterDTO> partialUpdate(SimilarityClusterDTO similarityClusterDTO);

    /**
     * Get all the similarityClusters.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SimilarityClusterDTO> findAll(Pageable pageable);

    /**
     * Get the "id" similarityCluster.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SimilarityClusterDTO> findOne(Long id);

    /**
     * Delete the "id" similarityCluster.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
