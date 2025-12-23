package fr.smartprod.paperdms.monitoring.service;

import fr.smartprod.paperdms.monitoring.service.dto.AlertDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link fr.smartprod.paperdms.monitoring.domain.Alert}.
 */
public interface AlertService {
    /**
     * Save a alert.
     *
     * @param alertDTO the entity to save.
     * @return the persisted entity.
     */
    AlertDTO save(AlertDTO alertDTO);

    /**
     * Updates a alert.
     *
     * @param alertDTO the entity to update.
     * @return the persisted entity.
     */
    AlertDTO update(AlertDTO alertDTO);

    /**
     * Partially updates a alert.
     *
     * @param alertDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AlertDTO> partialUpdate(AlertDTO alertDTO);

    /**
     * Get the "id" alert.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AlertDTO> findOne(Long id);

    /**
     * Delete the "id" alert.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
