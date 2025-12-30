package fr.smartprod.paperdms.workflow.service;

import fr.smartprod.paperdms.workflow.domain.*; // for static metamodels
import fr.smartprod.paperdms.workflow.domain.WorkflowApprovalHistory;
import fr.smartprod.paperdms.workflow.repository.WorkflowApprovalHistoryRepository;
import fr.smartprod.paperdms.workflow.repository.search.WorkflowApprovalHistorySearchRepository;
import fr.smartprod.paperdms.workflow.service.criteria.WorkflowApprovalHistoryCriteria;
import fr.smartprod.paperdms.workflow.service.dto.WorkflowApprovalHistoryDTO;
import fr.smartprod.paperdms.workflow.service.mapper.WorkflowApprovalHistoryMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link WorkflowApprovalHistory} entities in the database.
 * The main input is a {@link WorkflowApprovalHistoryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link WorkflowApprovalHistoryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class WorkflowApprovalHistoryQueryService extends QueryService<WorkflowApprovalHistory> {

    private static final Logger LOG = LoggerFactory.getLogger(WorkflowApprovalHistoryQueryService.class);

    private final WorkflowApprovalHistoryRepository workflowApprovalHistoryRepository;

    private final WorkflowApprovalHistoryMapper workflowApprovalHistoryMapper;

    private final WorkflowApprovalHistorySearchRepository workflowApprovalHistorySearchRepository;

    public WorkflowApprovalHistoryQueryService(
        WorkflowApprovalHistoryRepository workflowApprovalHistoryRepository,
        WorkflowApprovalHistoryMapper workflowApprovalHistoryMapper,
        WorkflowApprovalHistorySearchRepository workflowApprovalHistorySearchRepository
    ) {
        this.workflowApprovalHistoryRepository = workflowApprovalHistoryRepository;
        this.workflowApprovalHistoryMapper = workflowApprovalHistoryMapper;
        this.workflowApprovalHistorySearchRepository = workflowApprovalHistorySearchRepository;
    }

    /**
     * Return a {@link Page} of {@link WorkflowApprovalHistoryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<WorkflowApprovalHistoryDTO> findByCriteria(WorkflowApprovalHistoryCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<WorkflowApprovalHistory> specification = createSpecification(criteria);
        return workflowApprovalHistoryRepository.findAll(specification, page).map(workflowApprovalHistoryMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(WorkflowApprovalHistoryCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<WorkflowApprovalHistory> specification = createSpecification(criteria);
        return workflowApprovalHistoryRepository.count(specification);
    }

    /**
     * Function to convert {@link WorkflowApprovalHistoryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<WorkflowApprovalHistory> createSpecification(WorkflowApprovalHistoryCriteria criteria) {
        Specification<WorkflowApprovalHistory> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), WorkflowApprovalHistory_.id),
                buildStringSpecification(criteria.getDocumentSha256(), WorkflowApprovalHistory_.documentSha256),
                buildRangeSpecification(criteria.getStepNumber(), WorkflowApprovalHistory_.stepNumber),
                buildSpecification(criteria.getAction(), WorkflowApprovalHistory_.action),
                buildRangeSpecification(criteria.getActionDate(), WorkflowApprovalHistory_.actionDate),
                buildStringSpecification(criteria.getActionBy(), WorkflowApprovalHistory_.actionBy),
                buildStringSpecification(criteria.getPreviousAssignee(), WorkflowApprovalHistory_.previousAssignee),
                buildRangeSpecification(criteria.getTimeTaken(), WorkflowApprovalHistory_.timeTaken),
                buildSpecification(criteria.getWorkflowInstanceId(), root ->
                    root.join(WorkflowApprovalHistory_.workflowInstance, JoinType.LEFT).get(WorkflowInstance_.id)
                )
            );
        }
        return specification;
    }
}
