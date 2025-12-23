package fr.smartprod.paperdms.reporting.service;

import fr.smartprod.paperdms.reporting.service.dto.DashboardWidgetDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.smartprod.paperdms.reporting.domain.DashboardWidget}.
 */
public interface DashboardWidgetService {
    /**
     * Save a dashboardWidget.
     *
     * @param dashboardWidgetDTO the entity to save.
     * @return the persisted entity.
     */
    DashboardWidgetDTO save(DashboardWidgetDTO dashboardWidgetDTO);

    /**
     * Updates a dashboardWidget.
     *
     * @param dashboardWidgetDTO the entity to update.
     * @return the persisted entity.
     */
    DashboardWidgetDTO update(DashboardWidgetDTO dashboardWidgetDTO);

    /**
     * Partially updates a dashboardWidget.
     *
     * @param dashboardWidgetDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DashboardWidgetDTO> partialUpdate(DashboardWidgetDTO dashboardWidgetDTO);

    /**
     * Get all the dashboardWidgets.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DashboardWidgetDTO> findAll(Pageable pageable);

    /**
     * Get the "id" dashboardWidget.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DashboardWidgetDTO> findOne(Long id);

    /**
     * Delete the "id" dashboardWidget.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
