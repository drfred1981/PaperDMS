package fr.smartprod.paperdms.reporting.service;

import fr.smartprod.paperdms.reporting.service.dto.PerformanceMetricDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.smartprod.paperdms.reporting.domain.PerformanceMetric}.
 */
public interface PerformanceMetricService {
    /**
     * Save a performanceMetric.
     *
     * @param performanceMetricDTO the entity to save.
     * @return the persisted entity.
     */
    PerformanceMetricDTO save(PerformanceMetricDTO performanceMetricDTO);

    /**
     * Updates a performanceMetric.
     *
     * @param performanceMetricDTO the entity to update.
     * @return the persisted entity.
     */
    PerformanceMetricDTO update(PerformanceMetricDTO performanceMetricDTO);

    /**
     * Partially updates a performanceMetric.
     *
     * @param performanceMetricDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PerformanceMetricDTO> partialUpdate(PerformanceMetricDTO performanceMetricDTO);

    /**
     * Get all the performanceMetrics.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PerformanceMetricDTO> findAll(Pageable pageable);

    /**
     * Get the "id" performanceMetric.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PerformanceMetricDTO> findOne(Long id);

    /**
     * Delete the "id" performanceMetric.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
