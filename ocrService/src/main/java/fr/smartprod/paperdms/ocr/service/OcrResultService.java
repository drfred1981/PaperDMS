package fr.smartprod.paperdms.ocr.service;

import fr.smartprod.paperdms.ocr.service.dto.OcrResultDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.smartprod.paperdms.ocr.domain.OcrResult}.
 */
public interface OcrResultService {
    /**
     * Save a ocrResult.
     *
     * @param ocrResultDTO the entity to save.
     * @return the persisted entity.
     */
    OcrResultDTO save(OcrResultDTO ocrResultDTO);

    /**
     * Updates a ocrResult.
     *
     * @param ocrResultDTO the entity to update.
     * @return the persisted entity.
     */
    OcrResultDTO update(OcrResultDTO ocrResultDTO);

    /**
     * Partially updates a ocrResult.
     *
     * @param ocrResultDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OcrResultDTO> partialUpdate(OcrResultDTO ocrResultDTO);

    /**
     * Get all the ocrResults.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<OcrResultDTO> findAll(Pageable pageable);

    /**
     * Get the "id" ocrResult.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OcrResultDTO> findOne(Long id);

    /**
     * Delete the "id" ocrResult.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
