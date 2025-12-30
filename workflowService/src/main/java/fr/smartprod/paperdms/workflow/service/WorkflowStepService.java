package fr.smartprod.paperdms.workflow.service;

import fr.smartprod.paperdms.workflow.domain.WorkflowStep;
import fr.smartprod.paperdms.workflow.repository.WorkflowStepRepository;
import fr.smartprod.paperdms.workflow.repository.search.WorkflowStepSearchRepository;
import fr.smartprod.paperdms.workflow.service.dto.WorkflowStepDTO;
import fr.smartprod.paperdms.workflow.service.mapper.WorkflowStepMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.workflow.domain.WorkflowStep}.
 */
@Service
@Transactional
public class WorkflowStepService {

    private static final Logger LOG = LoggerFactory.getLogger(WorkflowStepService.class);

    private final WorkflowStepRepository workflowStepRepository;

    private final WorkflowStepMapper workflowStepMapper;

    private final WorkflowStepSearchRepository workflowStepSearchRepository;

    public WorkflowStepService(
        WorkflowStepRepository workflowStepRepository,
        WorkflowStepMapper workflowStepMapper,
        WorkflowStepSearchRepository workflowStepSearchRepository
    ) {
        this.workflowStepRepository = workflowStepRepository;
        this.workflowStepMapper = workflowStepMapper;
        this.workflowStepSearchRepository = workflowStepSearchRepository;
    }

    /**
     * Save a workflowStep.
     *
     * @param workflowStepDTO the entity to save.
     * @return the persisted entity.
     */
    public WorkflowStepDTO save(WorkflowStepDTO workflowStepDTO) {
        LOG.debug("Request to save WorkflowStep : {}", workflowStepDTO);
        WorkflowStep workflowStep = workflowStepMapper.toEntity(workflowStepDTO);
        workflowStep = workflowStepRepository.save(workflowStep);
        workflowStepSearchRepository.index(workflowStep);
        return workflowStepMapper.toDto(workflowStep);
    }

    /**
     * Update a workflowStep.
     *
     * @param workflowStepDTO the entity to save.
     * @return the persisted entity.
     */
    public WorkflowStepDTO update(WorkflowStepDTO workflowStepDTO) {
        LOG.debug("Request to update WorkflowStep : {}", workflowStepDTO);
        WorkflowStep workflowStep = workflowStepMapper.toEntity(workflowStepDTO);
        workflowStep = workflowStepRepository.save(workflowStep);
        workflowStepSearchRepository.index(workflowStep);
        return workflowStepMapper.toDto(workflowStep);
    }

    /**
     * Partially update a workflowStep.
     *
     * @param workflowStepDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<WorkflowStepDTO> partialUpdate(WorkflowStepDTO workflowStepDTO) {
        LOG.debug("Request to partially update WorkflowStep : {}", workflowStepDTO);

        return workflowStepRepository
            .findById(workflowStepDTO.getId())
            .map(existingWorkflowStep -> {
                workflowStepMapper.partialUpdate(existingWorkflowStep, workflowStepDTO);

                return existingWorkflowStep;
            })
            .map(workflowStepRepository::save)
            .map(savedWorkflowStep -> {
                workflowStepSearchRepository.index(savedWorkflowStep);
                return savedWorkflowStep;
            })
            .map(workflowStepMapper::toDto);
    }

    /**
     * Get one workflowStep by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<WorkflowStepDTO> findOne(Long id) {
        LOG.debug("Request to get WorkflowStep : {}", id);
        return workflowStepRepository.findById(id).map(workflowStepMapper::toDto);
    }

    /**
     * Delete the workflowStep by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete WorkflowStep : {}", id);
        workflowStepRepository.deleteById(id);
        workflowStepSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the workflowStep corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<WorkflowStepDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of WorkflowSteps for query {}", query);
        return workflowStepSearchRepository.search(query, pageable).map(workflowStepMapper::toDto);
    }
}
