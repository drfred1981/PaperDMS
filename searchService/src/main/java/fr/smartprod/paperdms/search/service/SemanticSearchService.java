package fr.smartprod.paperdms.search.service;

import fr.smartprod.paperdms.search.service.dto.SemanticSearchDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link fr.smartprod.paperdms.search.domain.SemanticSearch}.
 */
public interface SemanticSearchService {
    /**
     * Save a semanticSearch.
     *
     * @param semanticSearchDTO the entity to save.
     * @return the persisted entity.
     */
    SemanticSearchDTO save(SemanticSearchDTO semanticSearchDTO);

    /**
     * Updates a semanticSearch.
     *
     * @param semanticSearchDTO the entity to update.
     * @return the persisted entity.
     */
    SemanticSearchDTO update(SemanticSearchDTO semanticSearchDTO);

    /**
     * Partially updates a semanticSearch.
     *
     * @param semanticSearchDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SemanticSearchDTO> partialUpdate(SemanticSearchDTO semanticSearchDTO);

    /**
     * Get the "id" semanticSearch.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SemanticSearchDTO> findOne(Long id);

    /**
     * Delete the "id" semanticSearch.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
