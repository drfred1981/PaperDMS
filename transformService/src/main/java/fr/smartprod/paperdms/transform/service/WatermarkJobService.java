package fr.smartprod.paperdms.transform.service;

import fr.smartprod.paperdms.transform.service.dto.WatermarkJobDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link fr.smartprod.paperdms.transform.domain.WatermarkJob}.
 */
public interface WatermarkJobService {
    /**
     * Save a watermarkJob.
     *
     * @param watermarkJobDTO the entity to save.
     * @return the persisted entity.
     */
    WatermarkJobDTO save(WatermarkJobDTO watermarkJobDTO);

    /**
     * Updates a watermarkJob.
     *
     * @param watermarkJobDTO the entity to update.
     * @return the persisted entity.
     */
    WatermarkJobDTO update(WatermarkJobDTO watermarkJobDTO);

    /**
     * Partially updates a watermarkJob.
     *
     * @param watermarkJobDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<WatermarkJobDTO> partialUpdate(WatermarkJobDTO watermarkJobDTO);

    /**
     * Get the "id" watermarkJob.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<WatermarkJobDTO> findOne(Long id);

    /**
     * Delete the "id" watermarkJob.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
