package fr.smartprod.paperdms.workflow.service.impl;

import fr.smartprod.paperdms.workflow.domain.Workflow;
import fr.smartprod.paperdms.workflow.repository.WorkflowRepository;
import fr.smartprod.paperdms.workflow.repository.search.WorkflowSearchRepository;
import fr.smartprod.paperdms.workflow.service.WorkflowService;
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
public class WorkflowServiceImpl implements WorkflowService {

    private static final Logger LOG = LoggerFactory.getLogger(WorkflowServiceImpl.class);

    private final WorkflowRepository workflowRepository;

    private final WorkflowMapper workflowMapper;

    private final WorkflowSearchRepository workflowSearchRepository;

    public WorkflowServiceImpl(
        WorkflowRepository workflowRepository,
        WorkflowMapper workflowMapper,
        WorkflowSearchRepository workflowSearchRepository
    ) {
        this.workflowRepository = workflowRepository;
        this.workflowMapper = workflowMapper;
        this.workflowSearchRepository = workflowSearchRepository;
    }

    @Override
    public WorkflowDTO save(WorkflowDTO workflowDTO) {
        LOG.debug("Request to save Workflow : {}", workflowDTO);
        Workflow workflow = workflowMapper.toEntity(workflowDTO);
        workflow = workflowRepository.save(workflow);
        workflowSearchRepository.index(workflow);
        return workflowMapper.toDto(workflow);
    }

    @Override
    public WorkflowDTO update(WorkflowDTO workflowDTO) {
        LOG.debug("Request to update Workflow : {}", workflowDTO);
        Workflow workflow = workflowMapper.toEntity(workflowDTO);
        workflow = workflowRepository.save(workflow);
        workflowSearchRepository.index(workflow);
        return workflowMapper.toDto(workflow);
    }

    @Override
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

    @Override
    @Transactional(readOnly = true)
    public Optional<WorkflowDTO> findOne(Long id) {
        LOG.debug("Request to get Workflow : {}", id);
        return workflowRepository.findById(id).map(workflowMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Workflow : {}", id);
        workflowRepository.deleteById(id);
        workflowSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WorkflowDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of Workflows for query {}", query);
        return workflowSearchRepository.search(query, pageable).map(workflowMapper::toDto);
    }
}
