package fr.smartprod.paperdms.monitoring.service;

import fr.smartprod.paperdms.monitoring.service.dto.DocumentWatchDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link fr.smartprod.paperdms.monitoring.domain.DocumentWatch}.
 */
public interface DocumentWatchService {
    /**
     * Save a documentWatch.
     *
     * @param documentWatchDTO the entity to save.
     * @return the persisted entity.
     */
    DocumentWatchDTO save(DocumentWatchDTO documentWatchDTO);

    /**
     * Updates a documentWatch.
     *
     * @param documentWatchDTO the entity to update.
     * @return the persisted entity.
     */
    DocumentWatchDTO update(DocumentWatchDTO documentWatchDTO);

    /**
     * Partially updates a documentWatch.
     *
     * @param documentWatchDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DocumentWatchDTO> partialUpdate(DocumentWatchDTO documentWatchDTO);

    /**
     * Get the "id" documentWatch.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DocumentWatchDTO> findOne(Long id);

    /**
     * Delete the "id" documentWatch.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
