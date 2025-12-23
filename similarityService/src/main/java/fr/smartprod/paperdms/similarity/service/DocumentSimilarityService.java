package fr.smartprod.paperdms.similarity.service;

import fr.smartprod.paperdms.similarity.service.dto.DocumentSimilarityDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link fr.smartprod.paperdms.similarity.domain.DocumentSimilarity}.
 */
public interface DocumentSimilarityService {
    /**
     * Save a documentSimilarity.
     *
     * @param documentSimilarityDTO the entity to save.
     * @return the persisted entity.
     */
    DocumentSimilarityDTO save(DocumentSimilarityDTO documentSimilarityDTO);

    /**
     * Updates a documentSimilarity.
     *
     * @param documentSimilarityDTO the entity to update.
     * @return the persisted entity.
     */
    DocumentSimilarityDTO update(DocumentSimilarityDTO documentSimilarityDTO);

    /**
     * Partially updates a documentSimilarity.
     *
     * @param documentSimilarityDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DocumentSimilarityDTO> partialUpdate(DocumentSimilarityDTO documentSimilarityDTO);

    /**
     * Get the "id" documentSimilarity.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DocumentSimilarityDTO> findOne(Long id);

    /**
     * Delete the "id" documentSimilarity.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
