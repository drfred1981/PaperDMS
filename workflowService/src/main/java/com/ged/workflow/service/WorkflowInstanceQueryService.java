package com.ged.workflow.service;

import com.ged.workflow.domain.*; // for static metamodels
import com.ged.workflow.domain.WorkflowInstance;
import com.ged.workflow.repository.WorkflowInstanceRepository;
import com.ged.workflow.service.criteria.WorkflowInstanceCriteria;
import com.ged.workflow.service.dto.WorkflowInstanceDTO;
import com.ged.workflow.service.mapper.WorkflowInstanceMapper;
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
 * Service for executing complex queries for {@link WorkflowInstance} entities in the database.
 * The main input is a {@link WorkflowInstanceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link WorkflowInstanceDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class WorkflowInstanceQueryService extends QueryService<WorkflowInstance> {

    private static final Logger LOG = LoggerFactory.getLogger(WorkflowInstanceQueryService.class);

    private final WorkflowInstanceRepository workflowInstanceRepository;

    private final WorkflowInstanceMapper workflowInstanceMapper;

    public WorkflowInstanceQueryService(
        WorkflowInstanceRepository workflowInstanceRepository,
        WorkflowInstanceMapper workflowInstanceMapper
    ) {
        this.workflowInstanceRepository = workflowInstanceRepository;
        this.workflowInstanceMapper = workflowInstanceMapper;
    }

    /**
     * Return a {@link Page} of {@link WorkflowInstanceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<WorkflowInstanceDTO> findByCriteria(WorkflowInstanceCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<WorkflowInstance> specification = createSpecification(criteria);
        return workflowInstanceRepository.findAll(specification, page).map(workflowInstanceMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(WorkflowInstanceCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<WorkflowInstance> specification = createSpecification(criteria);
        return workflowInstanceRepository.count(specification);
    }

    /**
     * Function to convert {@link WorkflowInstanceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<WorkflowInstance> createSpecification(WorkflowInstanceCriteria criteria) {
        Specification<WorkflowInstance> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), WorkflowInstance_.id),
                buildRangeSpecification(criteria.getDocumentId(), WorkflowInstance_.documentId),
                buildSpecification(criteria.getStatus(), WorkflowInstance_.status),
                buildRangeSpecification(criteria.getCurrentStepNumber(), WorkflowInstance_.currentStepNumber),
                buildRangeSpecification(criteria.getStartDate(), WorkflowInstance_.startDate),
                buildRangeSpecification(criteria.getDueDate(), WorkflowInstance_.dueDate),
                buildRangeSpecification(criteria.getCompletedDate(), WorkflowInstance_.completedDate),
                buildRangeSpecification(criteria.getCancelledDate(), WorkflowInstance_.cancelledDate),
                buildSpecification(criteria.getPriority(), WorkflowInstance_.priority),
                buildStringSpecification(criteria.getCreatedBy(), WorkflowInstance_.createdBy),
                buildSpecification(criteria.getWorkflowId(), root -> root.join(WorkflowInstance_.workflow, JoinType.LEFT).get(Workflow_.id))
            );
        }
        return specification;
    }
}
