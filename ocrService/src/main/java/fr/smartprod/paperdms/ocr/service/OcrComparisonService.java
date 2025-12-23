package fr.smartprod.paperdms.ocr.service;

import fr.smartprod.paperdms.ocr.service.dto.OcrComparisonDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.smartprod.paperdms.ocr.domain.OcrComparison}.
 */
public interface OcrComparisonService {
    /**
     * Save a ocrComparison.
     *
     * @param ocrComparisonDTO the entity to save.
     * @return the persisted entity.
     */
    OcrComparisonDTO save(OcrComparisonDTO ocrComparisonDTO);

    /**
     * Updates a ocrComparison.
     *
     * @param ocrComparisonDTO the entity to update.
     * @return the persisted entity.
     */
    OcrComparisonDTO update(OcrComparisonDTO ocrComparisonDTO);

    /**
     * Partially updates a ocrComparison.
     *
     * @param ocrComparisonDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OcrComparisonDTO> partialUpdate(OcrComparisonDTO ocrComparisonDTO);

    /**
     * Get all the ocrComparisons.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<OcrComparisonDTO> findAll(Pageable pageable);

    /**
     * Get the "id" ocrComparison.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OcrComparisonDTO> findOne(Long id);

    /**
     * Delete the "id" ocrComparison.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
