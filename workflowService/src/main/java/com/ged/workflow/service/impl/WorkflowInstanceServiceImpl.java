package com.ged.workflow.service.impl;

import com.ged.workflow.domain.WorkflowInstance;
import com.ged.workflow.repository.WorkflowInstanceRepository;
import com.ged.workflow.service.WorkflowInstanceService;
import com.ged.workflow.service.dto.WorkflowInstanceDTO;
import com.ged.workflow.service.mapper.WorkflowInstanceMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.ged.workflow.domain.WorkflowInstance}.
 */
@Service
@Transactional
public class WorkflowInstanceServiceImpl implements WorkflowInstanceService {

    private static final Logger LOG = LoggerFactory.getLogger(WorkflowInstanceServiceImpl.class);

    private final WorkflowInstanceRepository workflowInstanceRepository;

    private final WorkflowInstanceMapper workflowInstanceMapper;

    public WorkflowInstanceServiceImpl(
        WorkflowInstanceRepository workflowInstanceRepository,
        WorkflowInstanceMapper workflowInstanceMapper
    ) {
        this.workflowInstanceRepository = workflowInstanceRepository;
        this.workflowInstanceMapper = workflowInstanceMapper;
    }

    @Override
    public WorkflowInstanceDTO save(WorkflowInstanceDTO workflowInstanceDTO) {
        LOG.debug("Request to save WorkflowInstance : {}", workflowInstanceDTO);
        WorkflowInstance workflowInstance = workflowInstanceMapper.toEntity(workflowInstanceDTO);
        workflowInstance = workflowInstanceRepository.save(workflowInstance);
        return workflowInstanceMapper.toDto(workflowInstance);
    }

    @Override
    public WorkflowInstanceDTO update(WorkflowInstanceDTO workflowInstanceDTO) {
        LOG.debug("Request to update WorkflowInstance : {}", workflowInstanceDTO);
        WorkflowInstance workflowInstance = workflowInstanceMapper.toEntity(workflowInstanceDTO);
        workflowInstance = workflowInstanceRepository.save(workflowInstance);
        return workflowInstanceMapper.toDto(workflowInstance);
    }

    @Override
    public Optional<WorkflowInstanceDTO> partialUpdate(WorkflowInstanceDTO workflowInstanceDTO) {
        LOG.debug("Request to partially update WorkflowInstance : {}", workflowInstanceDTO);

        return workflowInstanceRepository
            .findById(workflowInstanceDTO.getId())
            .map(existingWorkflowInstance -> {
                workflowInstanceMapper.partialUpdate(existingWorkflowInstance, workflowInstanceDTO);

                return existingWorkflowInstance;
            })
            .map(workflowInstanceRepository::save)
            .map(workflowInstanceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<WorkflowInstanceDTO> findOne(Long id) {
        LOG.debug("Request to get WorkflowInstance : {}", id);
        return workflowInstanceRepository.findById(id).map(workflowInstanceMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete WorkflowInstance : {}", id);
        workflowInstanceRepository.deleteById(id);
    }
}
