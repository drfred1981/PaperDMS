package fr.smartprod.paperdms.monitoring.service;

import fr.smartprod.paperdms.monitoring.domain.*; // for static metamodels
import fr.smartprod.paperdms.monitoring.domain.MaintenanceTask;
import fr.smartprod.paperdms.monitoring.repository.MaintenanceTaskRepository;
import fr.smartprod.paperdms.monitoring.service.criteria.MaintenanceTaskCriteria;
import fr.smartprod.paperdms.monitoring.service.dto.MaintenanceTaskDTO;
import fr.smartprod.paperdms.monitoring.service.mapper.MaintenanceTaskMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link MaintenanceTask} entities in the database.
 * The main input is a {@link MaintenanceTaskCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link MaintenanceTaskDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MaintenanceTaskQueryService extends QueryService<MaintenanceTask> {

    private static final Logger LOG = LoggerFactory.getLogger(MaintenanceTaskQueryService.class);

    private final MaintenanceTaskRepository maintenanceTaskRepository;

    private final MaintenanceTaskMapper maintenanceTaskMapper;

    public MaintenanceTaskQueryService(MaintenanceTaskRepository maintenanceTaskRepository, MaintenanceTaskMapper maintenanceTaskMapper) {
        this.maintenanceTaskRepository = maintenanceTaskRepository;
        this.maintenanceTaskMapper = maintenanceTaskMapper;
    }

    /**
     * Return a {@link Page} of {@link MaintenanceTaskDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MaintenanceTaskDTO> findByCriteria(MaintenanceTaskCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MaintenanceTask> specification = createSpecification(criteria);
        return maintenanceTaskRepository.findAll(specification, page).map(maintenanceTaskMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MaintenanceTaskCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<MaintenanceTask> specification = createSpecification(criteria);
        return maintenanceTaskRepository.count(specification);
    }

    /**
     * Function to convert {@link MaintenanceTaskCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MaintenanceTask> createSpecification(MaintenanceTaskCriteria criteria) {
        Specification<MaintenanceTask> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), MaintenanceTask_.id),
                buildStringSpecification(criteria.getName(), MaintenanceTask_.name),
                buildSpecification(criteria.getTaskType(), MaintenanceTask_.taskType),
                buildStringSpecification(criteria.getSchedule(), MaintenanceTask_.schedule),
                buildSpecification(criteria.getStatus(), MaintenanceTask_.status),
                buildSpecification(criteria.getIsActive(), MaintenanceTask_.isActive),
                buildRangeSpecification(criteria.getLastRun(), MaintenanceTask_.lastRun),
                buildRangeSpecification(criteria.getNextRun(), MaintenanceTask_.nextRun),
                buildRangeSpecification(criteria.getDuration(), MaintenanceTask_.duration),
                buildRangeSpecification(criteria.getRecordsProcessed(), MaintenanceTask_.recordsProcessed),
                buildStringSpecification(criteria.getCreatedBy(), MaintenanceTask_.createdBy),
                buildRangeSpecification(criteria.getCreatedDate(), MaintenanceTask_.createdDate)
            );
        }
        return specification;
    }
}
