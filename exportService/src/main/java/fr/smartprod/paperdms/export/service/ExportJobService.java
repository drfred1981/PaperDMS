package fr.smartprod.paperdms.export.service;

import fr.smartprod.paperdms.export.service.dto.ExportJobDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link fr.smartprod.paperdms.export.domain.ExportJob}.
 */
public interface ExportJobService {
    /**
     * Save a exportJob.
     *
     * @param exportJobDTO the entity to save.
     * @return the persisted entity.
     */
    ExportJobDTO save(ExportJobDTO exportJobDTO);

    /**
     * Updates a exportJob.
     *
     * @param exportJobDTO the entity to update.
     * @return the persisted entity.
     */
    ExportJobDTO update(ExportJobDTO exportJobDTO);

    /**
     * Partially updates a exportJob.
     *
     * @param exportJobDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ExportJobDTO> partialUpdate(ExportJobDTO exportJobDTO);

    /**
     * Get the "id" exportJob.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ExportJobDTO> findOne(Long id);

    /**
     * Delete the "id" exportJob.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
