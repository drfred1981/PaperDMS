package fr.smartprod.paperdms.workflow.service;

import fr.smartprod.paperdms.workflow.service.dto.WorkflowInstanceDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link fr.smartprod.paperdms.workflow.domain.WorkflowInstance}.
 */
public interface WorkflowInstanceService {
    /**
     * Save a workflowInstance.
     *
     * @param workflowInstanceDTO the entity to save.
     * @return the persisted entity.
     */
    WorkflowInstanceDTO save(WorkflowInstanceDTO workflowInstanceDTO);

    /**
     * Updates a workflowInstance.
     *
     * @param workflowInstanceDTO the entity to update.
     * @return the persisted entity.
     */
    WorkflowInstanceDTO update(WorkflowInstanceDTO workflowInstanceDTO);

    /**
     * Partially updates a workflowInstance.
     *
     * @param workflowInstanceDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<WorkflowInstanceDTO> partialUpdate(WorkflowInstanceDTO workflowInstanceDTO);

    /**
     * Get the "id" workflowInstance.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<WorkflowInstanceDTO> findOne(Long id);

    /**
     * Delete the "id" workflowInstance.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
