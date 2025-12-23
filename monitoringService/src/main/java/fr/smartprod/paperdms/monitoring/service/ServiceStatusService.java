package fr.smartprod.paperdms.monitoring.service;

import fr.smartprod.paperdms.monitoring.service.dto.ServiceStatusDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.smartprod.paperdms.monitoring.domain.ServiceStatus}.
 */
public interface ServiceStatusService {
    /**
     * Save a serviceStatus.
     *
     * @param serviceStatusDTO the entity to save.
     * @return the persisted entity.
     */
    ServiceStatusDTO save(ServiceStatusDTO serviceStatusDTO);

    /**
     * Updates a serviceStatus.
     *
     * @param serviceStatusDTO the entity to update.
     * @return the persisted entity.
     */
    ServiceStatusDTO update(ServiceStatusDTO serviceStatusDTO);

    /**
     * Partially updates a serviceStatus.
     *
     * @param serviceStatusDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ServiceStatusDTO> partialUpdate(ServiceStatusDTO serviceStatusDTO);

    /**
     * Get all the serviceStatuses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ServiceStatusDTO> findAll(Pageable pageable);

    /**
     * Get the "id" serviceStatus.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ServiceStatusDTO> findOne(Long id);

    /**
     * Delete the "id" serviceStatus.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
