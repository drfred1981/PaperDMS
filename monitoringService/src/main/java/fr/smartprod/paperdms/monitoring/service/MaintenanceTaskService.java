package fr.smartprod.paperdms.monitoring.service;

import fr.smartprod.paperdms.monitoring.service.dto.MaintenanceTaskDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link fr.smartprod.paperdms.monitoring.domain.MaintenanceTask}.
 */
public interface MaintenanceTaskService {
    /**
     * Save a maintenanceTask.
     *
     * @param maintenanceTaskDTO the entity to save.
     * @return the persisted entity.
     */
    MaintenanceTaskDTO save(MaintenanceTaskDTO maintenanceTaskDTO);

    /**
     * Updates a maintenanceTask.
     *
     * @param maintenanceTaskDTO the entity to update.
     * @return the persisted entity.
     */
    MaintenanceTaskDTO update(MaintenanceTaskDTO maintenanceTaskDTO);

    /**
     * Partially updates a maintenanceTask.
     *
     * @param maintenanceTaskDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MaintenanceTaskDTO> partialUpdate(MaintenanceTaskDTO maintenanceTaskDTO);

    /**
     * Get the "id" maintenanceTask.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MaintenanceTaskDTO> findOne(Long id);

    /**
     * Delete the "id" maintenanceTask.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
