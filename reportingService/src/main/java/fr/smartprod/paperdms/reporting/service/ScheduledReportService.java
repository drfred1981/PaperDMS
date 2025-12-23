package fr.smartprod.paperdms.reporting.service;

import fr.smartprod.paperdms.reporting.service.dto.ScheduledReportDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link fr.smartprod.paperdms.reporting.domain.ScheduledReport}.
 */
public interface ScheduledReportService {
    /**
     * Save a scheduledReport.
     *
     * @param scheduledReportDTO the entity to save.
     * @return the persisted entity.
     */
    ScheduledReportDTO save(ScheduledReportDTO scheduledReportDTO);

    /**
     * Updates a scheduledReport.
     *
     * @param scheduledReportDTO the entity to update.
     * @return the persisted entity.
     */
    ScheduledReportDTO update(ScheduledReportDTO scheduledReportDTO);

    /**
     * Partially updates a scheduledReport.
     *
     * @param scheduledReportDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ScheduledReportDTO> partialUpdate(ScheduledReportDTO scheduledReportDTO);

    /**
     * Get the "id" scheduledReport.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ScheduledReportDTO> findOne(Long id);

    /**
     * Delete the "id" scheduledReport.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
