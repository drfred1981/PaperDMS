package fr.smartprod.paperdms.ocr.service;

import fr.smartprod.paperdms.ocr.service.dto.OcrCacheDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.smartprod.paperdms.ocr.domain.OcrCache}.
 */
public interface OcrCacheService {
    /**
     * Save a ocrCache.
     *
     * @param ocrCacheDTO the entity to save.
     * @return the persisted entity.
     */
    OcrCacheDTO save(OcrCacheDTO ocrCacheDTO);

    /**
     * Updates a ocrCache.
     *
     * @param ocrCacheDTO the entity to update.
     * @return the persisted entity.
     */
    OcrCacheDTO update(OcrCacheDTO ocrCacheDTO);

    /**
     * Partially updates a ocrCache.
     *
     * @param ocrCacheDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OcrCacheDTO> partialUpdate(OcrCacheDTO ocrCacheDTO);

    /**
     * Get all the ocrCaches.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<OcrCacheDTO> findAll(Pageable pageable);

    /**
     * Get the "id" ocrCache.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OcrCacheDTO> findOne(Long id);

    /**
     * Delete the "id" ocrCache.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
