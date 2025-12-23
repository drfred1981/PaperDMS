package fr.smartprod.paperdms.workflow.service;

import fr.smartprod.paperdms.workflow.service.dto.WorkflowDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.smartprod.paperdms.workflow.domain.Workflow}.
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

    /**
     * Search for the workflow corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<WorkflowDTO> search(String query, Pageable pageable);
}
