package com.ged.search.service;

import com.ged.search.service.dto.SearchIndexDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.ged.search.domain.SearchIndex}.
 */
public interface SearchIndexService {
    /**
     * Save a searchIndex.
     *
     * @param searchIndexDTO the entity to save.
     * @return the persisted entity.
     */
    SearchIndexDTO save(SearchIndexDTO searchIndexDTO);

    /**
     * Updates a searchIndex.
     *
     * @param searchIndexDTO the entity to update.
     * @return the persisted entity.
     */
    SearchIndexDTO update(SearchIndexDTO searchIndexDTO);

    /**
     * Partially updates a searchIndex.
     *
     * @param searchIndexDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SearchIndexDTO> partialUpdate(SearchIndexDTO searchIndexDTO);

    /**
     * Get all the searchIndices.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SearchIndexDTO> findAll(Pageable pageable);

    /**
     * Get the "id" searchIndex.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SearchIndexDTO> findOne(Long id);

    /**
     * Delete the "id" searchIndex.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the searchIndex corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SearchIndexDTO> search(String query, Pageable pageable);
}
