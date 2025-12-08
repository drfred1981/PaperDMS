package com.ged.ai.service;

import com.ged.ai.service.dto.AutoTagJobDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.ged.ai.domain.AutoTagJob}.
 */
public interface AutoTagJobService {
    /**
     * Save a autoTagJob.
     *
     * @param autoTagJobDTO the entity to save.
     * @return the persisted entity.
     */
    AutoTagJobDTO save(AutoTagJobDTO autoTagJobDTO);

    /**
     * Updates a autoTagJob.
     *
     * @param autoTagJobDTO the entity to update.
     * @return the persisted entity.
     */
    AutoTagJobDTO update(AutoTagJobDTO autoTagJobDTO);

    /**
     * Partially updates a autoTagJob.
     *
     * @param autoTagJobDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AutoTagJobDTO> partialUpdate(AutoTagJobDTO autoTagJobDTO);

    /**
     * Get the "id" autoTagJob.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AutoTagJobDTO> findOne(Long id);

    /**
     * Delete the "id" autoTagJob.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
