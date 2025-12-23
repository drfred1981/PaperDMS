package fr.smartprod.paperdms.ai.service;

import fr.smartprod.paperdms.ai.service.dto.LanguageDetectionDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.smartprod.paperdms.ai.domain.LanguageDetection}.
 */
public interface LanguageDetectionService {
    /**
     * Save a languageDetection.
     *
     * @param languageDetectionDTO the entity to save.
     * @return the persisted entity.
     */
    LanguageDetectionDTO save(LanguageDetectionDTO languageDetectionDTO);

    /**
     * Updates a languageDetection.
     *
     * @param languageDetectionDTO the entity to update.
     * @return the persisted entity.
     */
    LanguageDetectionDTO update(LanguageDetectionDTO languageDetectionDTO);

    /**
     * Partially updates a languageDetection.
     *
     * @param languageDetectionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LanguageDetectionDTO> partialUpdate(LanguageDetectionDTO languageDetectionDTO);

    /**
     * Get all the languageDetections.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LanguageDetectionDTO> findAll(Pageable pageable);

    /**
     * Get the "id" languageDetection.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LanguageDetectionDTO> findOne(Long id);

    /**
     * Delete the "id" languageDetection.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
