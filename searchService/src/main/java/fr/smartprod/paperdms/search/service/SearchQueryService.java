package fr.smartprod.paperdms.search.service;

import fr.smartprod.paperdms.search.service.dto.SearchQueryDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link fr.smartprod.paperdms.search.domain.SearchQuery}.
 */
public interface SearchQueryService {
    /**
     * Save a searchQuery.
     *
     * @param searchQueryDTO the entity to save.
     * @return the persisted entity.
     */
    SearchQueryDTO save(SearchQueryDTO searchQueryDTO);

    /**
     * Updates a searchQuery.
     *
     * @param searchQueryDTO the entity to update.
     * @return the persisted entity.
     */
    SearchQueryDTO update(SearchQueryDTO searchQueryDTO);

    /**
     * Partially updates a searchQuery.
     *
     * @param searchQueryDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SearchQueryDTO> partialUpdate(SearchQueryDTO searchQueryDTO);

    /**
     * Get the "id" searchQuery.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SearchQueryDTO> findOne(Long id);

    /**
     * Delete the "id" searchQuery.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
