package fr.smartprod.paperdms.export.service;

import fr.smartprod.paperdms.export.service.dto.ExportResultDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.smartprod.paperdms.export.domain.ExportResult}.
 */
public interface ExportResultService {
    /**
     * Save a exportResult.
     *
     * @param exportResultDTO the entity to save.
     * @return the persisted entity.
     */
    ExportResultDTO save(ExportResultDTO exportResultDTO);

    /**
     * Updates a exportResult.
     *
     * @param exportResultDTO the entity to update.
     * @return the persisted entity.
     */
    ExportResultDTO update(ExportResultDTO exportResultDTO);

    /**
     * Partially updates a exportResult.
     *
     * @param exportResultDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ExportResultDTO> partialUpdate(ExportResultDTO exportResultDTO);

    /**
     * Get all the exportResults.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ExportResultDTO> findAll(Pageable pageable);

    /**
     * Get the "id" exportResult.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ExportResultDTO> findOne(Long id);

    /**
     * Delete the "id" exportResult.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
