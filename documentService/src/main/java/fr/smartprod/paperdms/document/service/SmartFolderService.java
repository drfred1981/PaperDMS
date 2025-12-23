package fr.smartprod.paperdms.document.service;

import fr.smartprod.paperdms.document.service.dto.SmartFolderDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link fr.smartprod.paperdms.document.domain.SmartFolder}.
 */
public interface SmartFolderService {
    /**
     * Save a smartFolder.
     *
     * @param smartFolderDTO the entity to save.
     * @return the persisted entity.
     */
    SmartFolderDTO save(SmartFolderDTO smartFolderDTO);

    /**
     * Updates a smartFolder.
     *
     * @param smartFolderDTO the entity to update.
     * @return the persisted entity.
     */
    SmartFolderDTO update(SmartFolderDTO smartFolderDTO);

    /**
     * Partially updates a smartFolder.
     *
     * @param smartFolderDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SmartFolderDTO> partialUpdate(SmartFolderDTO smartFolderDTO);

    /**
     * Get the "id" smartFolder.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SmartFolderDTO> findOne(Long id);

    /**
     * Delete the "id" smartFolder.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
