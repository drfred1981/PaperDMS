package com.ged.ocr.service;

import com.ged.ocr.service.dto.TikaConfigurationDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.ged.ocr.domain.TikaConfiguration}.
 */
public interface TikaConfigurationService {
    /**
     * Save a tikaConfiguration.
     *
     * @param tikaConfigurationDTO the entity to save.
     * @return the persisted entity.
     */
    TikaConfigurationDTO save(TikaConfigurationDTO tikaConfigurationDTO);

    /**
     * Updates a tikaConfiguration.
     *
     * @param tikaConfigurationDTO the entity to update.
     * @return the persisted entity.
     */
    TikaConfigurationDTO update(TikaConfigurationDTO tikaConfigurationDTO);

    /**
     * Partially updates a tikaConfiguration.
     *
     * @param tikaConfigurationDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TikaConfigurationDTO> partialUpdate(TikaConfigurationDTO tikaConfigurationDTO);

    /**
     * Get all the tikaConfigurations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TikaConfigurationDTO> findAll(Pageable pageable);

    /**
     * Get the "id" tikaConfiguration.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TikaConfigurationDTO> findOne(Long id);

    /**
     * Delete the "id" tikaConfiguration.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
