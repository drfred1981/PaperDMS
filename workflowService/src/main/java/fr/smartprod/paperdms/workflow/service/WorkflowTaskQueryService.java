package fr.smartprod.paperdms.workflow.service;

import fr.smartprod.paperdms.workflow.domain.*; // for static metamodels
import fr.smartprod.paperdms.workflow.domain.WorkflowTask;
import fr.smartprod.paperdms.workflow.repository.WorkflowTaskRepository;
import fr.smartprod.paperdms.workflow.service.criteria.WorkflowTaskCriteria;
import fr.smartprod.paperdms.workflow.service.dto.WorkflowTaskDTO;
import fr.smartprod.paperdms.workflow.service.mapper.WorkflowTaskMapper;
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
 * Service for executing complex queries for {@link WorkflowTask} entities in the database.
 * The main input is a {@link WorkflowTaskCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link WorkflowTaskDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class WorkflowTaskQueryService extends QueryService<WorkflowTask> {

    private static final Logger LOG = LoggerFactory.getLogger(WorkflowTaskQueryService.class);

    private final WorkflowTaskRepository workflowTaskRepository;

    private final WorkflowTaskMapper workflowTaskMapper;

    public WorkflowTaskQueryService(WorkflowTaskRepository workflowTaskRepository, WorkflowTaskMapper workflowTaskMapper) {
        this.workflowTaskRepository = workflowTaskRepository;
        this.workflowTaskMapper = workflowTaskMapper;
    }

    /**
     * Return a {@link Page} of {@link WorkflowTaskDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<WorkflowTaskDTO> findByCriteria(WorkflowTaskCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<WorkflowTask> specification = createSpecification(criteria);
        return workflowTaskRepository.findAll(specification, page).map(workflowTaskMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(WorkflowTaskCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<WorkflowTask> specification = createSpecification(criteria);
        return workflowTaskRepository.count(specification);
    }

    /**
     * Function to convert {@link WorkflowTaskCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<WorkflowTask> createSpecification(WorkflowTaskCriteria criteria) {
        Specification<WorkflowTask> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), WorkflowTask_.id),
                buildStringSpecification(criteria.getAssigneeId(), WorkflowTask_.assigneeId),
                buildSpecification(criteria.getStatus(), WorkflowTask_.status),
                buildSpecification(criteria.getAction(), WorkflowTask_.action),
                buildRangeSpecification(criteria.getAssignedDate(), WorkflowTask_.assignedDate),
                buildRangeSpecification(criteria.getDueDate(), WorkflowTask_.dueDate),
                buildRangeSpecification(criteria.getCompletedDate(), WorkflowTask_.completedDate),
                buildSpecification(criteria.getReminderSent(), WorkflowTask_.reminderSent),
                buildStringSpecification(criteria.getDelegatedTo(), WorkflowTask_.delegatedTo),
                buildRangeSpecification(criteria.getDelegatedDate(), WorkflowTask_.delegatedDate),
                buildSpecification(criteria.getInstanceId(), root ->
                    root.join(WorkflowTask_.instance, JoinType.LEFT).get(WorkflowInstance_.id)
                ),
                buildSpecification(criteria.getStepId(), root -> root.join(WorkflowTask_.step, JoinType.LEFT).get(WorkflowStep_.id))
            );
        }
        return specification;
    }
}
