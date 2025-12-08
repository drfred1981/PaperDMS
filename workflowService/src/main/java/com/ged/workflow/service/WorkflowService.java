package com.ged.workflow.service;

import com.ged.workflow.service.dto.WorkflowDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.ged.workflow.domain.Workflow}.
 */
public interface WorkflowService {
    /**
     * Save a workflow.
     *
     * @param workflowDTO the entity to save.
     * @return the persisted entity.
     */
    WorkflowDTO save(WorkflowDTO workflowDTO);

    /**
     * Updates a workflow.
     *
     * @param workflowDTO the entity to update.
     * @return the persisted entity.
     */
    WorkflowDTO update(WorkflowDTO workflowDTO);

    /**
     * Partially updates a workflow.
     *
     * @param workflowDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<WorkflowDTO> partialUpdate(WorkflowDTO workflowDTO);

    /**
     * Get the "id" workflow.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<WorkflowDTO> findOne(Long id);

    /**
     * Delete the "id" workflow.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
