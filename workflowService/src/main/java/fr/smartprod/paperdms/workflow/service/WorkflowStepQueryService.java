package fr.smartprod.paperdms.workflow.service;

import fr.smartprod.paperdms.workflow.domain.*; // for static metamodels
import fr.smartprod.paperdms.workflow.domain.WorkflowStep;
import fr.smartprod.paperdms.workflow.repository.WorkflowStepRepository;
import fr.smartprod.paperdms.workflow.repository.search.WorkflowStepSearchRepository;
import fr.smartprod.paperdms.workflow.service.criteria.WorkflowStepCriteria;
import fr.smartprod.paperdms.workflow.service.dto.WorkflowStepDTO;
import fr.smartprod.paperdms.workflow.service.mapper.WorkflowStepMapper;
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
 * Service for executing complex queries for {@link WorkflowStep} entities in the database.
 * The main input is a {@link WorkflowStepCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link WorkflowStepDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class WorkflowStepQueryService extends QueryService<WorkflowStep> {

    private static final Logger LOG = LoggerFactory.getLogger(WorkflowStepQueryService.class);

    private final WorkflowStepRepository workflowStepRepository;

    private final WorkflowStepMapper workflowStepMapper;

    private final WorkflowStepSearchRepository workflowStepSearchRepository;

    public WorkflowStepQueryService(
        WorkflowStepRepository workflowStepRepository,
        WorkflowStepMapper workflowStepMapper,
        WorkflowStepSearchRepository workflowStepSearchRepository
    ) {
        this.workflowStepRepository = workflowStepRepository;
        this.workflowStepMapper = workflowStepMapper;
        this.workflowStepSearchRepository = workflowStepSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link WorkflowStepDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<WorkflowStepDTO> findByCriteria(WorkflowStepCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<WorkflowStep> specification = createSpecification(criteria);
        return workflowStepRepository.findAll(specification, page).map(workflowStepMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(WorkflowStepCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<WorkflowStep> specification = createSpecification(criteria);
        return workflowStepRepository.count(specification);
    }

    /**
     * Function to convert {@link WorkflowStepCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<WorkflowStep> createSpecification(WorkflowStepCriteria criteria) {
        Specification<WorkflowStep> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), WorkflowStep_.id),
                buildRangeSpecification(criteria.getStepNumber(), WorkflowStep_.stepNumber),
                buildStringSpecification(criteria.getName(), WorkflowStep_.name),
                buildSpecification(criteria.getStepType(), WorkflowStep_.stepType),
                buildSpecification(criteria.getAssigneeType(), WorkflowStep_.assigneeType),
                buildStringSpecification(criteria.getAssigneeId(), WorkflowStep_.assigneeId),
                buildStringSpecification(criteria.getAssigneeGroup(), WorkflowStep_.assigneeGroup),
                buildRangeSpecification(criteria.getDueInDays(), WorkflowStep_.dueInDays),
                buildSpecification(criteria.getIsRequired(), WorkflowStep_.isRequired),
                buildSpecification(criteria.getCanDelegate(), WorkflowStep_.canDelegate),
                buildSpecification(criteria.getCanReject(), WorkflowStep_.canReject),
                buildSpecification(criteria.getWorkflowTasksId(), root ->
                    root.join(WorkflowStep_.workflowTasks, JoinType.LEFT).get(WorkflowTask_.id)
                ),
                buildSpecification(criteria.getWorkflowId(), root -> root.join(WorkflowStep_.workflow, JoinType.LEFT).get(Workflow_.id))
            );
        }
        return specification;
    }
}
