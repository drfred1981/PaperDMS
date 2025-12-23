package fr.smartprod.paperdms.workflow.service;

import fr.smartprod.paperdms.workflow.service.dto.WorkflowTaskDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link fr.smartprod.paperdms.workflow.domain.WorkflowTask}.
 */
public interface WorkflowTaskService {
    /**
     * Save a workflowTask.
     *
     * @param workflowTaskDTO the entity to save.
     * @return the persisted entity.
     */
    WorkflowTaskDTO save(WorkflowTaskDTO workflowTaskDTO);

    /**
     * Updates a workflowTask.
     *
     * @param workflowTaskDTO the entity to update.
     * @return the persisted entity.
     */
    WorkflowTaskDTO update(WorkflowTaskDTO workflowTaskDTO);

    /**
     * Partially updates a workflowTask.
     *
     * @param workflowTaskDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<WorkflowTaskDTO> partialUpdate(WorkflowTaskDTO workflowTaskDTO);

    /**
     * Get the "id" workflowTask.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<WorkflowTaskDTO> findOne(Long id);

    /**
     * Delete the "id" workflowTask.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
