package fr.smartprod.paperdms.document.service;

import fr.smartprod.paperdms.document.service.dto.DocumentCommentDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link fr.smartprod.paperdms.document.domain.DocumentComment}.
 */
public interface DocumentCommentService {
    /**
     * Save a documentComment.
     *
     * @param documentCommentDTO the entity to save.
     * @return the persisted entity.
     */
    DocumentCommentDTO save(DocumentCommentDTO documentCommentDTO);

    /**
     * Updates a documentComment.
     *
     * @param documentCommentDTO the entity to update.
     * @return the persisted entity.
     */
    DocumentCommentDTO update(DocumentCommentDTO documentCommentDTO);

    /**
     * Partially updates a documentComment.
     *
     * @param documentCommentDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DocumentCommentDTO> partialUpdate(DocumentCommentDTO documentCommentDTO);

    /**
     * Get the "id" documentComment.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DocumentCommentDTO> findOne(Long id);

    /**
     * Delete the "id" documentComment.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
