package fr.smartprod.paperdms.workflow.service;

import fr.smartprod.paperdms.workflow.domain.*; // for static metamodels
import fr.smartprod.paperdms.workflow.domain.Workflow;
import fr.smartprod.paperdms.workflow.repository.WorkflowRepository;
import fr.smartprod.paperdms.workflow.repository.search.WorkflowSearchRepository;
import fr.smartprod.paperdms.workflow.service.criteria.WorkflowCriteria;
import fr.smartprod.paperdms.workflow.service.dto.WorkflowDTO;
import fr.smartprod.paperdms.workflow.service.mapper.WorkflowMapper;
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
 * Service for executing complex queries for {@link Workflow} entities in the database.
 * The main input is a {@link WorkflowCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link WorkflowDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class WorkflowQueryService extends QueryService<Workflow> {

    private static final Logger LOG = LoggerFactory.getLogger(WorkflowQueryService.class);

    private final WorkflowRepository workflowRepository;

    private final WorkflowMapper workflowMapper;

    private final WorkflowSearchRepository workflowSearchRepository;

    public WorkflowQueryService(
        WorkflowRepository workflowRepository,
        WorkflowMapper workflowMapper,
        WorkflowSearchRepository workflowSearchRepository
    ) {
        this.workflowRepository = workflowRepository;
        this.workflowMapper = workflowMapper;
        this.workflowSearchRepository = workflowSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link WorkflowDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<WorkflowDTO> findByCriteria(WorkflowCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Workflow> specification = createSpecification(criteria);
        return workflowRepository.findAll(specification, page).map(workflowMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(WorkflowCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Workflow> specification = createSpecification(criteria);
        return workflowRepository.count(specification);
    }

    /**
     * Function to convert {@link WorkflowCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Workflow> createSpecification(WorkflowCriteria criteria) {
        Specification<Workflow> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Workflow_.id),
                buildStringSpecification(criteria.getName(), Workflow_.name),
                buildRangeSpecification(criteria.getVersion(), Workflow_.version),
                buildSpecification(criteria.getIsActive(), Workflow_.isActive),
                buildSpecification(criteria.getIsParallel(), Workflow_.isParallel),
                buildSpecification(criteria.getAutoStart(), Workflow_.autoStart),
                buildStringSpecification(criteria.getTriggerEvent(), Workflow_.triggerEvent),
                buildRangeSpecification(criteria.getCreatedDate(), Workflow_.createdDate),
                buildRangeSpecification(criteria.getLastModifiedDate(), Workflow_.lastModifiedDate),
                buildStringSpecification(criteria.getCreatedBy(), Workflow_.createdBy),
                buildSpecification(criteria.getWorkflowStpesId(), root ->
                    root.join(Workflow_.workflowStpes, JoinType.LEFT).get(WorkflowStep_.id)
                ),
                buildSpecification(criteria.getWorkflowInstancesId(), root ->
                    root.join(Workflow_.workflowInstances, JoinType.LEFT).get(WorkflowInstance_.id)
                )
            );
        }
        return specification;
    }
}
