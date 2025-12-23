package fr.smartprod.paperdms.document.service;

import fr.smartprod.paperdms.document.service.dto.DocumentVersionDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.smartprod.paperdms.document.domain.DocumentVersion}.
 */
public interface DocumentVersionService {
    /**
     * Save a documentVersion.
     *
     * @param documentVersionDTO the entity to save.
     * @return the persisted entity.
     */
    DocumentVersionDTO save(DocumentVersionDTO documentVersionDTO);

    /**
     * Updates a documentVersion.
     *
     * @param documentVersionDTO the entity to update.
     * @return the persisted entity.
     */
    DocumentVersionDTO update(DocumentVersionDTO documentVersionDTO);

    /**
     * Partially updates a documentVersion.
     *
     * @param documentVersionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DocumentVersionDTO> partialUpdate(DocumentVersionDTO documentVersionDTO);

    /**
     * Get all the documentVersions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DocumentVersionDTO> findAll(Pageable pageable);

    /**
     * Get the "id" documentVersion.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DocumentVersionDTO> findOne(Long id);

    /**
     * Delete the "id" documentVersion.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
