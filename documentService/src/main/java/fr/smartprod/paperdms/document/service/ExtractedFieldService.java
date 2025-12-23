package fr.smartprod.paperdms.document.service;

import fr.smartprod.paperdms.document.service.dto.ExtractedFieldDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.smartprod.paperdms.document.domain.ExtractedField}.
 */
public interface ExtractedFieldService {
    /**
     * Save a extractedField.
     *
     * @param extractedFieldDTO the entity to save.
     * @return the persisted entity.
     */
    ExtractedFieldDTO save(ExtractedFieldDTO extractedFieldDTO);

    /**
     * Updates a extractedField.
     *
     * @param extractedFieldDTO the entity to update.
     * @return the persisted entity.
     */
    ExtractedFieldDTO update(ExtractedFieldDTO extractedFieldDTO);

    /**
     * Partially updates a extractedField.
     *
     * @param extractedFieldDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ExtractedFieldDTO> partialUpdate(ExtractedFieldDTO extractedFieldDTO);

    /**
     * Get all the extractedFields.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ExtractedFieldDTO> findAll(Pageable pageable);

    /**
     * Get the "id" extractedField.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ExtractedFieldDTO> findOne(Long id);

    /**
     * Delete the "id" extractedField.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the extractedField corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ExtractedFieldDTO> search(String query, Pageable pageable);
}
