package fr.smartprod.paperdms.transform.service;

import fr.smartprod.paperdms.transform.service.dto.ConversionJobDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link fr.smartprod.paperdms.transform.domain.ConversionJob}.
 */
public interface ConversionJobService {
    /**
     * Save a conversionJob.
     *
     * @param conversionJobDTO the entity to save.
     * @return the persisted entity.
     */
    ConversionJobDTO save(ConversionJobDTO conversionJobDTO);

    /**
     * Updates a conversionJob.
     *
     * @param conversionJobDTO the entity to update.
     * @return the persisted entity.
     */
    ConversionJobDTO update(ConversionJobDTO conversionJobDTO);

    /**
     * Partially updates a conversionJob.
     *
     * @param conversionJobDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ConversionJobDTO> partialUpdate(ConversionJobDTO conversionJobDTO);

    /**
     * Get the "id" conversionJob.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ConversionJobDTO> findOne(Long id);

    /**
     * Delete the "id" conversionJob.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
