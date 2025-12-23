package fr.smartprod.paperdms.document.service;

import fr.smartprod.paperdms.document.service.dto.DocumentTagDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.smartprod.paperdms.document.domain.DocumentTag}.
 */
public interface DocumentTagService {
    /**
     * Save a documentTag.
     *
     * @param documentTagDTO the entity to save.
     * @return the persisted entity.
     */
    DocumentTagDTO save(DocumentTagDTO documentTagDTO);

    /**
     * Updates a documentTag.
     *
     * @param documentTagDTO the entity to update.
     * @return the persisted entity.
     */
    DocumentTagDTO update(DocumentTagDTO documentTagDTO);

    /**
     * Partially updates a documentTag.
     *
     * @param documentTagDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DocumentTagDTO> partialUpdate(DocumentTagDTO documentTagDTO);

    /**
     * Get all the documentTags.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DocumentTagDTO> findAll(Pageable pageable);

    /**
     * Get the "id" documentTag.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DocumentTagDTO> findOne(Long id);

    /**
     * Delete the "id" documentTag.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
