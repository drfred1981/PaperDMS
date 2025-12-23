package fr.smartprod.paperdms.monitoring.service;

import fr.smartprod.paperdms.monitoring.service.dto.SystemHealthDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.smartprod.paperdms.monitoring.domain.SystemHealth}.
 */
public interface SystemHealthService {
    /**
     * Save a systemHealth.
     *
     * @param systemHealthDTO the entity to save.
     * @return the persisted entity.
     */
    SystemHealthDTO save(SystemHealthDTO systemHealthDTO);

    /**
     * Updates a systemHealth.
     *
     * @param systemHealthDTO the entity to update.
     * @return the persisted entity.
     */
    SystemHealthDTO update(SystemHealthDTO systemHealthDTO);

    /**
     * Partially updates a systemHealth.
     *
     * @param systemHealthDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SystemHealthDTO> partialUpdate(SystemHealthDTO systemHealthDTO);

    /**
     * Get all the systemHealths.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SystemHealthDTO> findAll(Pageable pageable);

    /**
     * Get the "id" systemHealth.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SystemHealthDTO> findOne(Long id);

    /**
     * Delete the "id" systemHealth.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
