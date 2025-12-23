package fr.smartprod.paperdms.workflow.service;

import fr.smartprod.paperdms.workflow.service.dto.WorkflowStepDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.smartprod.paperdms.workflow.domain.WorkflowStep}.
 */
public interface WorkflowStepService {
    /**
     * Save a workflowStep.
     *
     * @param workflowStepDTO the entity to save.
     * @return the persisted entity.
     */
    WorkflowStepDTO save(WorkflowStepDTO workflowStepDTO);

    /**
     * Updates a workflowStep.
     *
     * @param workflowStepDTO the entity to update.
     * @return the persisted entity.
     */
    WorkflowStepDTO update(WorkflowStepDTO workflowStepDTO);

    /**
     * Partially updates a workflowStep.
     *
     * @param workflowStepDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<WorkflowStepDTO> partialUpdate(WorkflowStepDTO workflowStepDTO);

    /**
     * Get all the workflowSteps.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<WorkflowStepDTO> findAll(Pageable pageable);

    /**
     * Get the "id" workflowStep.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<WorkflowStepDTO> findOne(Long id);

    /**
     * Delete the "id" workflowStep.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
