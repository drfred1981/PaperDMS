package fr.smartprod.paperdms.document.service;

import fr.smartprod.paperdms.document.service.dto.DocumentRelationDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link fr.smartprod.paperdms.document.domain.DocumentRelation}.
 */
public interface DocumentRelationService {
    /**
     * Save a documentRelation.
     *
     * @param documentRelationDTO the entity to save.
     * @return the persisted entity.
     */
    DocumentRelationDTO save(DocumentRelationDTO documentRelationDTO);

    /**
     * Updates a documentRelation.
     *
     * @param documentRelationDTO the entity to update.
     * @return the persisted entity.
     */
    DocumentRelationDTO update(DocumentRelationDTO documentRelationDTO);

    /**
     * Partially updates a documentRelation.
     *
     * @param documentRelationDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DocumentRelationDTO> partialUpdate(DocumentRelationDTO documentRelationDTO);

    /**
     * Get the "id" documentRelation.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DocumentRelationDTO> findOne(Long id);

    /**
     * Delete the "id" documentRelation.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
