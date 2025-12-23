package fr.smartprod.paperdms.document.service;

import fr.smartprod.paperdms.document.service.dto.PermissionGroupDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.smartprod.paperdms.document.domain.PermissionGroup}.
 */
public interface PermissionGroupService {
    /**
     * Save a permissionGroup.
     *
     * @param permissionGroupDTO the entity to save.
     * @return the persisted entity.
     */
    PermissionGroupDTO save(PermissionGroupDTO permissionGroupDTO);

    /**
     * Updates a permissionGroup.
     *
     * @param permissionGroupDTO the entity to update.
     * @return the persisted entity.
     */
    PermissionGroupDTO update(PermissionGroupDTO permissionGroupDTO);

    /**
     * Partially updates a permissionGroup.
     *
     * @param permissionGroupDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PermissionGroupDTO> partialUpdate(PermissionGroupDTO permissionGroupDTO);

    /**
     * Get all the permissionGroups.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PermissionGroupDTO> findAll(Pageable pageable);

    /**
     * Get the "id" permissionGroup.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PermissionGroupDTO> findOne(Long id);

    /**
     * Delete the "id" permissionGroup.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
