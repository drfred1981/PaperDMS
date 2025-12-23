package fr.smartprod.paperdms.document.service;

import fr.smartprod.paperdms.document.service.dto.DocumentPermissionDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link fr.smartprod.paperdms.document.domain.DocumentPermission}.
 */
public interface DocumentPermissionService {
    /**
     * Save a documentPermission.
     *
     * @param documentPermissionDTO the entity to save.
     * @return the persisted entity.
     */
    DocumentPermissionDTO save(DocumentPermissionDTO documentPermissionDTO);

    /**
     * Updates a documentPermission.
     *
     * @param documentPermissionDTO the entity to update.
     * @return the persisted entity.
     */
    DocumentPermissionDTO update(DocumentPermissionDTO documentPermissionDTO);

    /**
     * Partially updates a documentPermission.
     *
     * @param documentPermissionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DocumentPermissionDTO> partialUpdate(DocumentPermissionDTO documentPermissionDTO);

    /**
     * Get the "id" documentPermission.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DocumentPermissionDTO> findOne(Long id);

    /**
     * Delete the "id" documentPermission.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
