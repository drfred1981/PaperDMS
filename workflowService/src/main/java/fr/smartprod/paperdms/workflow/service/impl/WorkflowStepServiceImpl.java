package fr.smartprod.paperdms.workflow.service.impl;

import fr.smartprod.paperdms.workflow.domain.WorkflowStep;
import fr.smartprod.paperdms.workflow.repository.WorkflowStepRepository;
import fr.smartprod.paperdms.workflow.service.WorkflowStepService;
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
public class WorkflowStepServiceImpl implements WorkflowStepService {

    private static final Logger LOG = LoggerFactory.getLogger(WorkflowStepServiceImpl.class);

    private final WorkflowStepRepository workflowStepRepository;

    private final WorkflowStepMapper workflowStepMapper;

    public WorkflowStepServiceImpl(WorkflowStepRepository workflowStepRepository, WorkflowStepMapper workflowStepMapper) {
        this.workflowStepRepository = workflowStepRepository;
        this.workflowStepMapper = workflowStepMapper;
    }

    @Override
    public WorkflowStepDTO save(WorkflowStepDTO workflowStepDTO) {
        LOG.debug("Request to save WorkflowStep : {}", workflowStepDTO);
        WorkflowStep workflowStep = workflowStepMapper.toEntity(workflowStepDTO);
        workflowStep = workflowStepRepository.save(workflowStep);
        return workflowStepMapper.toDto(workflowStep);
    }

    @Override
    public WorkflowStepDTO update(WorkflowStepDTO workflowStepDTO) {
        LOG.debug("Request to update WorkflowStep : {}", workflowStepDTO);
        WorkflowStep workflowStep = workflowStepMapper.toEntity(workflowStepDTO);
        workflowStep = workflowStepRepository.save(workflowStep);
        return workflowStepMapper.toDto(workflowStep);
    }

    @Override
    public Optional<WorkflowStepDTO> partialUpdate(WorkflowStepDTO workflowStepDTO) {
        LOG.debug("Request to partially update WorkflowStep : {}", workflowStepDTO);

        return workflowStepRepository
            .findById(workflowStepDTO.getId())
            .map(existingWorkflowStep -> {
                workflowStepMapper.partialUpdate(existingWorkflowStep, workflowStepDTO);

                return existingWorkflowStep;
            })
            .map(workflowStepRepository::save)
            .map(workflowStepMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WorkflowStepDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all WorkflowSteps");
        return workflowStepRepository.findAll(pageable).map(workflowStepMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<WorkflowStepDTO> findOne(Long id) {
        LOG.debug("Request to get WorkflowStep : {}", id);
        return workflowStepRepository.findById(id).map(workflowStepMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete WorkflowStep : {}", id);
        workflowStepRepository.deleteById(id);
    }
}
