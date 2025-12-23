package fr.smartprod.paperdms.search.service;

import fr.smartprod.paperdms.search.service.dto.SearchFacetDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.smartprod.paperdms.search.domain.SearchFacet}.
 */
public interface SearchFacetService {
    /**
     * Save a searchFacet.
     *
     * @param searchFacetDTO the entity to save.
     * @return the persisted entity.
     */
    SearchFacetDTO save(SearchFacetDTO searchFacetDTO);

    /**
     * Updates a searchFacet.
     *
     * @param searchFacetDTO the entity to update.
     * @return the persisted entity.
     */
    SearchFacetDTO update(SearchFacetDTO searchFacetDTO);

    /**
     * Partially updates a searchFacet.
     *
     * @param searchFacetDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SearchFacetDTO> partialUpdate(SearchFacetDTO searchFacetDTO);

    /**
     * Get all the searchFacets.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SearchFacetDTO> findAll(Pageable pageable);

    /**
     * Get the "id" searchFacet.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SearchFacetDTO> findOne(Long id);

    /**
     * Delete the "id" searchFacet.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
