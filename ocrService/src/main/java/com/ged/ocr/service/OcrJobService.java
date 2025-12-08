package com.ged.ocr.service;

import com.ged.ocr.service.dto.OcrJobDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.ged.ocr.domain.OcrJob}.
 */
public interface OcrJobService {
    /**
     * Save a ocrJob.
     *
     * @param ocrJobDTO the entity to save.
     * @return the persisted entity.
     */
    OcrJobDTO save(OcrJobDTO ocrJobDTO);

    /**
     * Updates a ocrJob.
     *
     * @param ocrJobDTO the entity to update.
     * @return the persisted entity.
     */
    OcrJobDTO update(OcrJobDTO ocrJobDTO);

    /**
     * Partially updates a ocrJob.
     *
     * @param ocrJobDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OcrJobDTO> partialUpdate(OcrJobDTO ocrJobDTO);

    /**
     * Get the "id" ocrJob.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OcrJobDTO> findOne(Long id);

    /**
     * Delete the "id" ocrJob.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
