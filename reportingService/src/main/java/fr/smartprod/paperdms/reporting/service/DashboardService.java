package fr.smartprod.paperdms.reporting.service;

import fr.smartprod.paperdms.reporting.service.dto.DashboardDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link fr.smartprod.paperdms.reporting.domain.Dashboard}.
 */
public interface DashboardService {
    /**
     * Save a dashboard.
     *
     * @param dashboardDTO the entity to save.
     * @return the persisted entity.
     */
    DashboardDTO save(DashboardDTO dashboardDTO);

    /**
     * Updates a dashboard.
     *
     * @param dashboardDTO the entity to update.
     * @return the persisted entity.
     */
    DashboardDTO update(DashboardDTO dashboardDTO);

    /**
     * Partially updates a dashboard.
     *
     * @param dashboardDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DashboardDTO> partialUpdate(DashboardDTO dashboardDTO);

    /**
     * Get the "id" dashboard.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DashboardDTO> findOne(Long id);

    /**
     * Delete the "id" dashboard.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
