package fr.smartprod.paperdms.scan.service;

import fr.smartprod.paperdms.scan.service.dto.ScanJobDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link fr.smartprod.paperdms.scan.domain.ScanJob}.
 */
public interface ScanJobService {
    /**
     * Save a scanJob.
     *
     * @param scanJobDTO the entity to save.
     * @return the persisted entity.
     */
    ScanJobDTO save(ScanJobDTO scanJobDTO);

    /**
     * Updates a scanJob.
     *
     * @param scanJobDTO the entity to update.
     * @return the persisted entity.
     */
    ScanJobDTO update(ScanJobDTO scanJobDTO);

    /**
     * Partially updates a scanJob.
     *
     * @param scanJobDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ScanJobDTO> partialUpdate(ScanJobDTO scanJobDTO);

    /**
     * Get the "id" scanJob.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ScanJobDTO> findOne(Long id);

    /**
     * Delete the "id" scanJob.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
