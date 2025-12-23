package fr.smartprod.paperdms.export.service;

import fr.smartprod.paperdms.export.service.dto.ExportPatternDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link fr.smartprod.paperdms.export.domain.ExportPattern}.
 */
public interface ExportPatternService {
    /**
     * Save a exportPattern.
     *
     * @param exportPatternDTO the entity to save.
     * @return the persisted entity.
     */
    ExportPatternDTO save(ExportPatternDTO exportPatternDTO);

    /**
     * Updates a exportPattern.
     *
     * @param exportPatternDTO the entity to update.
     * @return the persisted entity.
     */
    ExportPatternDTO update(ExportPatternDTO exportPatternDTO);

    /**
     * Partially updates a exportPattern.
     *
     * @param exportPatternDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ExportPatternDTO> partialUpdate(ExportPatternDTO exportPatternDTO);

    /**
     * Get the "id" exportPattern.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ExportPatternDTO> findOne(Long id);

    /**
     * Delete the "id" exportPattern.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
