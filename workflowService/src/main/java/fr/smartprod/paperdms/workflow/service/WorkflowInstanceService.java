package fr.smartprod.paperdms.workflow.service;

import fr.smartprod.paperdms.workflow.domain.WorkflowInstance;
import fr.smartprod.paperdms.workflow.repository.WorkflowInstanceRepository;
import fr.smartprod.paperdms.workflow.repository.search.WorkflowInstanceSearchRepository;
import fr.smartprod.paperdms.workflow.service.dto.WorkflowInstanceDTO;
import fr.smartprod.paperdms.workflow.service.mapper.WorkflowInstanceMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.workflow.domain.WorkflowInstance}.
 */
@Service
@Transactional
public class WorkflowInstanceService {

    private static final Logger LOG = LoggerFactory.getLogger(WorkflowInstanceService.class);

    private final WorkflowInstanceRepository workflowInstanceRepository;

    private final WorkflowInstanceMapper workflowInstanceMapper;

    private final WorkflowInstanceSearchRepository workflowInstanceSearchRepository;

    public WorkflowInstanceService(
        WorkflowInstanceRepository workflowInstanceRepository,
        WorkflowInstanceMapper workflowInstanceMapper,
        WorkflowInstanceSearchRepository workflowInstanceSearchRepository
    ) {
        this.workflowInstanceRepository = workflowInstanceRepository;
        this.workflowInstanceMapper = workflowInstanceMapper;
        this.workflowInstanceSearchRepository = workflowInstanceSearchRepository;
    }

    /**
     * Save a workflowInstance.
     *
     * @param workflowInstanceDTO the entity to save.
     * @return the persisted entity.
     */
    public WorkflowInstanceDTO save(WorkflowInstanceDTO workflowInstanceDTO) {
        LOG.debug("Request to save WorkflowInstance : {}", workflowInstanceDTO);
        WorkflowInstance workflowInstance = workflowInstanceMapper.toEntity(workflowInstanceDTO);
        workflowInstance = workflowInstanceRepository.save(workflowInstance);
        workflowInstanceSearchRepository.index(workflowInstance);
        return workflowInstanceMapper.toDto(workflowInstance);
    }

    /**
     * Update a workflowInstance.
     *
     * @param workflowInstanceDTO the entity to save.
     * @return the persisted entity.
     */
    public WorkflowInstanceDTO update(WorkflowInstanceDTO workflowInstanceDTO) {
        LOG.debug("Request to update WorkflowInstance : {}", workflowInstanceDTO);
        WorkflowInstance workflowInstance = workflowInstanceMapper.toEntity(workflowInstanceDTO);
        workflowInstance = workflowInstanceRepository.save(workflowInstance);
        workflowInstanceSearchRepository.index(workflowInstance);
        return workflowInstanceMapper.toDto(workflowInstance);
    }

    /**
     * Partially update a workflowInstance.
     *
     * @param workflowInstanceDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<WorkflowInstanceDTO> partialUpdate(WorkflowInstanceDTO workflowInstanceDTO) {
        LOG.debug("Request to partially update WorkflowInstance : {}", workflowInstanceDTO);

        return workflowInstanceRepository
            .findById(workflowInstanceDTO.getId())
            .map(existingWorkflowInstance -> {
                workflowInstanceMapper.partialUpdate(existingWorkflowInstance, workflowInstanceDTO);

                return existingWorkflowInstance;
            })
            .map(workflowInstanceRepository::save)
            .map(savedWorkflowInstance -> {
                workflowInstanceSearchRepository.index(savedWorkflowInstance);
                return savedWorkflowInstance;
            })
            .map(workflowInstanceMapper::toDto);
    }

    /**
     * Get one workflowInstance by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<WorkflowInstanceDTO> findOne(Long id) {
        LOG.debug("Request to get WorkflowInstance : {}", id);
        return workflowInstanceRepository.findById(id).map(workflowInstanceMapper::toDto);
    }

    /**
     * Delete the workflowInstance by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete WorkflowInstance : {}", id);
        workflowInstanceRepository.deleteById(id);
        workflowInstanceSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the workflowInstance corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<WorkflowInstanceDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of WorkflowInstances for query {}", query);
        return workflowInstanceSearchRepository.search(query, pageable).map(workflowInstanceMapper::toDto);
    }
}
