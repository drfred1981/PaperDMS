package fr.smartprod.paperdms.scan.service;

import fr.smartprod.paperdms.scan.service.dto.ScanBatchDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link fr.smartprod.paperdms.scan.domain.ScanBatch}.
 */
public interface ScanBatchService {
    /**
     * Save a scanBatch.
     *
     * @param scanBatchDTO the entity to save.
     * @return the persisted entity.
     */
    ScanBatchDTO save(ScanBatchDTO scanBatchDTO);

    /**
     * Updates a scanBatch.
     *
     * @param scanBatchDTO the entity to update.
     * @return the persisted entity.
     */
    ScanBatchDTO update(ScanBatchDTO scanBatchDTO);

    /**
     * Partially updates a scanBatch.
     *
     * @param scanBatchDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ScanBatchDTO> partialUpdate(ScanBatchDTO scanBatchDTO);

    /**
     * Get the "id" scanBatch.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ScanBatchDTO> findOne(Long id);

    /**
     * Delete the "id" scanBatch.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
