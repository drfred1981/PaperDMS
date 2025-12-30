package fr.smartprod.paperdms.workflow.service;

import fr.smartprod.paperdms.workflow.domain.WorkflowTask;
import fr.smartprod.paperdms.workflow.repository.WorkflowTaskRepository;
import fr.smartprod.paperdms.workflow.repository.search.WorkflowTaskSearchRepository;
import fr.smartprod.paperdms.workflow.service.dto.WorkflowTaskDTO;
import fr.smartprod.paperdms.workflow.service.mapper.WorkflowTaskMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.workflow.domain.WorkflowTask}.
 */
@Service
@Transactional
public class WorkflowTaskService {

    private static final Logger LOG = LoggerFactory.getLogger(WorkflowTaskService.class);

    private final WorkflowTaskRepository workflowTaskRepository;

    private final WorkflowTaskMapper workflowTaskMapper;

    private final WorkflowTaskSearchRepository workflowTaskSearchRepository;

    public WorkflowTaskService(
        WorkflowTaskRepository workflowTaskRepository,
        WorkflowTaskMapper workflowTaskMapper,
        WorkflowTaskSearchRepository workflowTaskSearchRepository
    ) {
        this.workflowTaskRepository = workflowTaskRepository;
        this.workflowTaskMapper = workflowTaskMapper;
        this.workflowTaskSearchRepository = workflowTaskSearchRepository;
    }

    /**
     * Save a workflowTask.
     *
     * @param workflowTaskDTO the entity to save.
     * @return the persisted entity.
     */
    public WorkflowTaskDTO save(WorkflowTaskDTO workflowTaskDTO) {
        LOG.debug("Request to save WorkflowTask : {}", workflowTaskDTO);
        WorkflowTask workflowTask = workflowTaskMapper.toEntity(workflowTaskDTO);
        workflowTask = workflowTaskRepository.save(workflowTask);
        workflowTaskSearchRepository.index(workflowTask);
        return workflowTaskMapper.toDto(workflowTask);
    }

    /**
     * Update a workflowTask.
     *
     * @param workflowTaskDTO the entity to save.
     * @return the persisted entity.
     */
    public WorkflowTaskDTO update(WorkflowTaskDTO workflowTaskDTO) {
        LOG.debug("Request to update WorkflowTask : {}", workflowTaskDTO);
        WorkflowTask workflowTask = workflowTaskMapper.toEntity(workflowTaskDTO);
        workflowTask = workflowTaskRepository.save(workflowTask);
        workflowTaskSearchRepository.index(workflowTask);
        return workflowTaskMapper.toDto(workflowTask);
    }

    /**
     * Partially update a workflowTask.
     *
     * @param workflowTaskDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<WorkflowTaskDTO> partialUpdate(WorkflowTaskDTO workflowTaskDTO) {
        LOG.debug("Request to partially update WorkflowTask : {}", workflowTaskDTO);

        return workflowTaskRepository
            .findById(workflowTaskDTO.getId())
            .map(existingWorkflowTask -> {
                workflowTaskMapper.partialUpdate(existingWorkflowTask, workflowTaskDTO);

                return existingWorkflowTask;
            })
            .map(workflowTaskRepository::save)
            .map(savedWorkflowTask -> {
                workflowTaskSearchRepository.index(savedWorkflowTask);
                return savedWorkflowTask;
            })
            .map(workflowTaskMapper::toDto);
    }

    /**
     * Get one workflowTask by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<WorkflowTaskDTO> findOne(Long id) {
        LOG.debug("Request to get WorkflowTask : {}", id);
        return workflowTaskRepository.findById(id).map(workflowTaskMapper::toDto);
    }

    /**
     * Delete the workflowTask by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete WorkflowTask : {}", id);
        workflowTaskRepository.deleteById(id);
        workflowTaskSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the workflowTask corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<WorkflowTaskDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of WorkflowTasks for query {}", query);
        return workflowTaskSearchRepository.search(query, pageable).map(workflowTaskMapper::toDto);
    }
}
