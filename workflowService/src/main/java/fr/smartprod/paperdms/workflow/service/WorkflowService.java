package fr.smartprod.paperdms.workflow.service;

import fr.smartprod.paperdms.workflow.domain.Workflow;
import fr.smartprod.paperdms.workflow.repository.WorkflowRepository;
import fr.smartprod.paperdms.workflow.repository.search.WorkflowSearchRepository;
import fr.smartprod.paperdms.workflow.service.dto.WorkflowDTO;
import fr.smartprod.paperdms.workflow.service.mapper.WorkflowMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.workflow.domain.Workflow}.
 */
@Service
@Transactional
public class WorkflowService {

    private static final Logger LOG = LoggerFactory.getLogger(WorkflowService.class);

    private final WorkflowRepository workflowRepository;

    private final WorkflowMapper workflowMapper;

    private final WorkflowSearchRepository workflowSearchRepository;

    public WorkflowService(
        WorkflowRepository workflowRepository,
        WorkflowMapper workflowMapper,
        WorkflowSearchRepository workflowSearchRepository
    ) {
        this.workflowRepository = workflowRepository;
        this.workflowMapper = workflowMapper;
        this.workflowSearchRepository = workflowSearchRepository;
    }

    /**
     * Save a workflow.
     *
     * @param workflowDTO the entity to save.
     * @return the persisted entity.
     */
    public WorkflowDTO save(WorkflowDTO workflowDTO) {
        LOG.debug("Request to save Workflow : {}", workflowDTO);
        Workflow workflow = workflowMapper.toEntity(workflowDTO);
        workflow = workflowRepository.save(workflow);
        workflowSearchRepository.index(workflow);
        return workflowMapper.toDto(workflow);
    }

    /**
     * Update a workflow.
     *
     * @param workflowDTO the entity to save.
     * @return the persisted entity.
     */
    public WorkflowDTO update(WorkflowDTO workflowDTO) {
        LOG.debug("Request to update Workflow : {}", workflowDTO);
        Workflow workflow = workflowMapper.toEntity(workflowDTO);
        workflow = workflowRepository.save(workflow);
        workflowSearchRepository.index(workflow);
        return workflowMapper.toDto(workflow);
    }

    /**
     * Partially update a workflow.
     *
     * @param workflowDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<WorkflowDTO> partialUpdate(WorkflowDTO workflowDTO) {
        LOG.debug("Request to partially update Workflow : {}", workflowDTO);

        return workflowRepository
            .findById(workflowDTO.getId())
            .map(existingWorkflow -> {
                workflowMapper.partialUpdate(existingWorkflow, workflowDTO);

                return existingWorkflow;
            })
            .map(workflowRepository::save)
            .map(savedWorkflow -> {
                workflowSearchRepository.index(savedWorkflow);
                return savedWorkflow;
            })
            .map(workflowMapper::toDto);
    }

    /**
     * Get one workflow by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<WorkflowDTO> findOne(Long id) {
        LOG.debug("Request to get Workflow : {}", id);
        return workflowRepository.findById(id).map(workflowMapper::toDto);
    }

    /**
     * Delete the workflow by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Workflow : {}", id);
        workflowRepository.deleteById(id);
        workflowSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the workflow corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<WorkflowDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of Workflows for query {}", query);
        return workflowSearchRepository.search(query, pageable).map(workflowMapper::toDto);
    }
}
