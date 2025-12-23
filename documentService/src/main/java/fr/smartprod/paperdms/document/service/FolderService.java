package fr.smartprod.paperdms.document.service;

import fr.smartprod.paperdms.document.service.dto.FolderDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link fr.smartprod.paperdms.document.domain.Folder}.
 */
public interface FolderService {
    /**
     * Save a folder.
     *
     * @param folderDTO the entity to save.
     * @return the persisted entity.
     */
    FolderDTO save(FolderDTO folderDTO);

    /**
     * Updates a folder.
     *
     * @param folderDTO the entity to update.
     * @return the persisted entity.
     */
    FolderDTO update(FolderDTO folderDTO);

    /**
     * Partially updates a folder.
     *
     * @param folderDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FolderDTO> partialUpdate(FolderDTO folderDTO);

    /**
     * Get the "id" folder.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FolderDTO> findOne(Long id);

    /**
     * Delete the "id" folder.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
