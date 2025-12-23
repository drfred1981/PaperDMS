package fr.smartprod.paperdms.document.service;

import fr.smartprod.paperdms.document.service.dto.SavedSearchDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link fr.smartprod.paperdms.document.domain.SavedSearch}.
 */
public interface SavedSearchService {
    /**
     * Save a savedSearch.
     *
     * @param savedSearchDTO the entity to save.
     * @return the persisted entity.
     */
    SavedSearchDTO save(SavedSearchDTO savedSearchDTO);

    /**
     * Updates a savedSearch.
     *
     * @param savedSearchDTO the entity to update.
     * @return the persisted entity.
     */
    SavedSearchDTO update(SavedSearchDTO savedSearchDTO);

    /**
     * Partially updates a savedSearch.
     *
     * @param savedSearchDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SavedSearchDTO> partialUpdate(SavedSearchDTO savedSearchDTO);

    /**
     * Get the "id" savedSearch.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SavedSearchDTO> findOne(Long id);

    /**
     * Delete the "id" savedSearch.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
