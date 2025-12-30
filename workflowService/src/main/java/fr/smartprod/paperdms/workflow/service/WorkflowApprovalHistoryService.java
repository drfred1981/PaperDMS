package fr.smartprod.paperdms.workflow.service;

import fr.smartprod.paperdms.workflow.domain.WorkflowApprovalHistory;
import fr.smartprod.paperdms.workflow.repository.WorkflowApprovalHistoryRepository;
import fr.smartprod.paperdms.workflow.repository.search.WorkflowApprovalHistorySearchRepository;
import fr.smartprod.paperdms.workflow.service.dto.WorkflowApprovalHistoryDTO;
import fr.smartprod.paperdms.workflow.service.mapper.WorkflowApprovalHistoryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.workflow.domain.WorkflowApprovalHistory}.
 */
@Service
@Transactional
public class WorkflowApprovalHistoryService {

    private static final Logger LOG = LoggerFactory.getLogger(WorkflowApprovalHistoryService.class);

    private final WorkflowApprovalHistoryRepository workflowApprovalHistoryRepository;

    private final WorkflowApprovalHistoryMapper workflowApprovalHistoryMapper;

    private final WorkflowApprovalHistorySearchRepository workflowApprovalHistorySearchRepository;

    public WorkflowApprovalHistoryService(
        WorkflowApprovalHistoryRepository workflowApprovalHistoryRepository,
        WorkflowApprovalHistoryMapper workflowApprovalHistoryMapper,
        WorkflowApprovalHistorySearchRepository workflowApprovalHistorySearchRepository
    ) {
        this.workflowApprovalHistoryRepository = workflowApprovalHistoryRepository;
        this.workflowApprovalHistoryMapper = workflowApprovalHistoryMapper;
        this.workflowApprovalHistorySearchRepository = workflowApprovalHistorySearchRepository;
    }

    /**
     * Save a workflowApprovalHistory.
     *
     * @param workflowApprovalHistoryDTO the entity to save.
     * @return the persisted entity.
     */
    public WorkflowApprovalHistoryDTO save(WorkflowApprovalHistoryDTO workflowApprovalHistoryDTO) {
        LOG.debug("Request to save WorkflowApprovalHistory : {}", workflowApprovalHistoryDTO);
        WorkflowApprovalHistory workflowApprovalHistory = workflowApprovalHistoryMapper.toEntity(workflowApprovalHistoryDTO);
        workflowApprovalHistory = workflowApprovalHistoryRepository.save(workflowApprovalHistory);
        workflowApprovalHistorySearchRepository.index(workflowApprovalHistory);
        return workflowApprovalHistoryMapper.toDto(workflowApprovalHistory);
    }

    /**
     * Update a workflowApprovalHistory.
     *
     * @param workflowApprovalHistoryDTO the entity to save.
     * @return the persisted entity.
     */
    public WorkflowApprovalHistoryDTO update(WorkflowApprovalHistoryDTO workflowApprovalHistoryDTO) {
        LOG.debug("Request to update WorkflowApprovalHistory : {}", workflowApprovalHistoryDTO);
        WorkflowApprovalHistory workflowApprovalHistory = workflowApprovalHistoryMapper.toEntity(workflowApprovalHistoryDTO);
        workflowApprovalHistory = workflowApprovalHistoryRepository.save(workflowApprovalHistory);
        workflowApprovalHistorySearchRepository.index(workflowApprovalHistory);
        return workflowApprovalHistoryMapper.toDto(workflowApprovalHistory);
    }

    /**
     * Partially update a workflowApprovalHistory.
     *
     * @param workflowApprovalHistoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<WorkflowApprovalHistoryDTO> partialUpdate(WorkflowApprovalHistoryDTO workflowApprovalHistoryDTO) {
        LOG.debug("Request to partially update WorkflowApprovalHistory : {}", workflowApprovalHistoryDTO);

        return workflowApprovalHistoryRepository
            .findById(workflowApprovalHistoryDTO.getId())
            .map(existingWorkflowApprovalHistory -> {
                workflowApprovalHistoryMapper.partialUpdate(existingWorkflowApprovalHistory, workflowApprovalHistoryDTO);

                return existingWorkflowApprovalHistory;
            })
            .map(workflowApprovalHistoryRepository::save)
            .map(savedWorkflowApprovalHistory -> {
                workflowApprovalHistorySearchRepository.index(savedWorkflowApprovalHistory);
                return savedWorkflowApprovalHistory;
            })
            .map(workflowApprovalHistoryMapper::toDto);
    }

    /**
     * Get one workflowApprovalHistory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<WorkflowApprovalHistoryDTO> findOne(Long id) {
        LOG.debug("Request to get WorkflowApprovalHistory : {}", id);
        return workflowApprovalHistoryRepository.findById(id).map(workflowApprovalHistoryMapper::toDto);
    }

    /**
     * Delete the workflowApprovalHistory by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete WorkflowApprovalHistory : {}", id);
        workflowApprovalHistoryRepository.deleteById(id);
        workflowApprovalHistorySearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the workflowApprovalHistory corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<WorkflowApprovalHistoryDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of WorkflowApprovalHistories for query {}", query);
        return workflowApprovalHistorySearchRepository.search(query, pageable).map(workflowApprovalHistoryMapper::toDto);
    }
}
