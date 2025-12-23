package fr.smartprod.paperdms.reporting.service;

import fr.smartprod.paperdms.reporting.service.dto.SystemMetricDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.smartprod.paperdms.reporting.domain.SystemMetric}.
 */
public interface SystemMetricService {
    /**
     * Save a systemMetric.
     *
     * @param systemMetricDTO the entity to save.
     * @return the persisted entity.
     */
    SystemMetricDTO save(SystemMetricDTO systemMetricDTO);

    /**
     * Updates a systemMetric.
     *
     * @param systemMetricDTO the entity to update.
     * @return the persisted entity.
     */
    SystemMetricDTO update(SystemMetricDTO systemMetricDTO);

    /**
     * Partially updates a systemMetric.
     *
     * @param systemMetricDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SystemMetricDTO> partialUpdate(SystemMetricDTO systemMetricDTO);

    /**
     * Get all the systemMetrics.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SystemMetricDTO> findAll(Pageable pageable);

    /**
     * Get the "id" systemMetric.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SystemMetricDTO> findOne(Long id);

    /**
     * Delete the "id" systemMetric.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
