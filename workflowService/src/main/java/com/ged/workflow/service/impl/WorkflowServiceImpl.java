package com.ged.workflow.service.impl;

import com.ged.workflow.domain.Workflow;
import com.ged.workflow.repository.WorkflowRepository;
import com.ged.workflow.service.WorkflowService;
import com.ged.workflow.service.dto.WorkflowDTO;
import com.ged.workflow.service.mapper.WorkflowMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.ged.workflow.domain.Workflow}.
 */
@Service
@Transactional
public class WorkflowServiceImpl implements WorkflowService {

    private static final Logger LOG = LoggerFactory.getLogger(WorkflowServiceImpl.class);

    private final WorkflowRepository workflowRepository;

    private final WorkflowMapper workflowMapper;

    public WorkflowServiceImpl(WorkflowRepository workflowRepository, WorkflowMapper workflowMapper) {
        this.workflowRepository = workflowRepository;
        this.workflowMapper = workflowMapper;
    }

    @Override
    public WorkflowDTO save(WorkflowDTO workflowDTO) {
        LOG.debug("Request to save Workflow : {}", workflowDTO);
        Workflow workflow = workflowMapper.toEntity(workflowDTO);
        workflow = workflowRepository.save(workflow);
        return workflowMapper.toDto(workflow);
    }

    @Override
    public WorkflowDTO update(WorkflowDTO workflowDTO) {
        LOG.debug("Request to update Workflow : {}", workflowDTO);
        Workflow workflow = workflowMapper.toEntity(workflowDTO);
        workflow = workflowRepository.save(workflow);
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
    }
}
