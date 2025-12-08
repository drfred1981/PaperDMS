package com.ged.workflow.service.impl;

import com.ged.workflow.domain.WorkflowTask;
import com.ged.workflow.repository.WorkflowTaskRepository;
import com.ged.workflow.service.WorkflowTaskService;
import com.ged.workflow.service.dto.WorkflowTaskDTO;
import com.ged.workflow.service.mapper.WorkflowTaskMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.ged.workflow.domain.WorkflowTask}.
 */
@Service
@Transactional
public class WorkflowTaskServiceImpl implements WorkflowTaskService {

    private static final Logger LOG = LoggerFactory.getLogger(WorkflowTaskServiceImpl.class);

    private final WorkflowTaskRepository workflowTaskRepository;

    private final WorkflowTaskMapper workflowTaskMapper;

    public WorkflowTaskServiceImpl(WorkflowTaskRepository workflowTaskRepository, WorkflowTaskMapper workflowTaskMapper) {
        this.workflowTaskRepository = workflowTaskRepository;
        this.workflowTaskMapper = workflowTaskMapper;
    }

    @Override
    public WorkflowTaskDTO save(WorkflowTaskDTO workflowTaskDTO) {
        LOG.debug("Request to save WorkflowTask : {}", workflowTaskDTO);
        WorkflowTask workflowTask = workflowTaskMapper.toEntity(workflowTaskDTO);
        workflowTask = workflowTaskRepository.save(workflowTask);
        return workflowTaskMapper.toDto(workflowTask);
    }

    @Override
    public WorkflowTaskDTO update(WorkflowTaskDTO workflowTaskDTO) {
        LOG.debug("Request to update WorkflowTask : {}", workflowTaskDTO);
        WorkflowTask workflowTask = workflowTaskMapper.toEntity(workflowTaskDTO);
        workflowTask = workflowTaskRepository.save(workflowTask);
        return workflowTaskMapper.toDto(workflowTask);
    }

    @Override
    public Optional<WorkflowTaskDTO> partialUpdate(WorkflowTaskDTO workflowTaskDTO) {
        LOG.debug("Request to partially update WorkflowTask : {}", workflowTaskDTO);

        return workflowTaskRepository
            .findById(workflowTaskDTO.getId())
            .map(existingWorkflowTask -> {
                workflowTaskMapper.partialUpdate(existingWorkflowTask, workflowTaskDTO);

                return existingWorkflowTask;
            })
            .map(workflowTaskRepository::save)
            .map(workflowTaskMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<WorkflowTaskDTO> findOne(Long id) {
        LOG.debug("Request to get WorkflowTask : {}", id);
        return workflowTaskRepository.findById(id).map(workflowTaskMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete WorkflowTask : {}", id);
        workflowTaskRepository.deleteById(id);
    }
}
