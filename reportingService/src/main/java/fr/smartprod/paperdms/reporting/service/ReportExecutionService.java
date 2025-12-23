package fr.smartprod.paperdms.reporting.service;

import fr.smartprod.paperdms.reporting.service.dto.ReportExecutionDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.smartprod.paperdms.reporting.domain.ReportExecution}.
 */
public interface ReportExecutionService {
    /**
     * Save a reportExecution.
     *
     * @param reportExecutionDTO the entity to save.
     * @return the persisted entity.
     */
    ReportExecutionDTO save(ReportExecutionDTO reportExecutionDTO);

    /**
     * Updates a reportExecution.
     *
     * @param reportExecutionDTO the entity to update.
     * @return the persisted entity.
     */
    ReportExecutionDTO update(ReportExecutionDTO reportExecutionDTO);

    /**
     * Partially updates a reportExecution.
     *
     * @param reportExecutionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ReportExecutionDTO> partialUpdate(ReportExecutionDTO reportExecutionDTO);

    /**
     * Get all the reportExecutions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ReportExecutionDTO> findAll(Pageable pageable);

    /**
     * Get the "id" reportExecution.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ReportExecutionDTO> findOne(Long id);

    /**
     * Delete the "id" reportExecution.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
