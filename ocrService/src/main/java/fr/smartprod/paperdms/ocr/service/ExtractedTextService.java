package fr.smartprod.paperdms.ocr.service;

import fr.smartprod.paperdms.ocr.service.dto.ExtractedTextDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.smartprod.paperdms.ocr.domain.ExtractedText}.
 */
public interface ExtractedTextService {
    /**
     * Save a extractedText.
     *
     * @param extractedTextDTO the entity to save.
     * @return the persisted entity.
     */
    ExtractedTextDTO save(ExtractedTextDTO extractedTextDTO);

    /**
     * Updates a extractedText.
     *
     * @param extractedTextDTO the entity to update.
     * @return the persisted entity.
     */
    ExtractedTextDTO update(ExtractedTextDTO extractedTextDTO);

    /**
     * Partially updates a extractedText.
     *
     * @param extractedTextDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ExtractedTextDTO> partialUpdate(ExtractedTextDTO extractedTextDTO);

    /**
     * Get all the extractedTexts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ExtractedTextDTO> findAll(Pageable pageable);

    /**
     * Get the "id" extractedText.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ExtractedTextDTO> findOne(Long id);

    /**
     * Delete the "id" extractedText.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the extractedText corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ExtractedTextDTO> search(String query, Pageable pageable);
}
