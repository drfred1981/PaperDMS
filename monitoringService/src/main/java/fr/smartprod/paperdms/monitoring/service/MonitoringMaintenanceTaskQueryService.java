package fr.smartprod.paperdms.monitoring.service;

import fr.smartprod.paperdms.monitoring.domain.*; // for static metamodels
import fr.smartprod.paperdms.monitoring.domain.MonitoringMaintenanceTask;
import fr.smartprod.paperdms.monitoring.repository.MonitoringMaintenanceTaskRepository;
import fr.smartprod.paperdms.monitoring.service.criteria.MonitoringMaintenanceTaskCriteria;
import fr.smartprod.paperdms.monitoring.service.dto.MonitoringMaintenanceTaskDTO;
import fr.smartprod.paperdms.monitoring.service.mapper.MonitoringMaintenanceTaskMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link MonitoringMaintenanceTask} entities in the database.
 * The main input is a {@link MonitoringMaintenanceTaskCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link MonitoringMaintenanceTaskDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MonitoringMaintenanceTaskQueryService extends QueryService<MonitoringMaintenanceTask> {

    private static final Logger LOG = LoggerFactory.getLogger(MonitoringMaintenanceTaskQueryService.class);

    private final MonitoringMaintenanceTaskRepository monitoringMaintenanceTaskRepository;

    private final MonitoringMaintenanceTaskMapper monitoringMaintenanceTaskMapper;

    public MonitoringMaintenanceTaskQueryService(
        MonitoringMaintenanceTaskRepository monitoringMaintenanceTaskRepository,
        MonitoringMaintenanceTaskMapper monitoringMaintenanceTaskMapper
    ) {
        this.monitoringMaintenanceTaskRepository = monitoringMaintenanceTaskRepository;
        this.monitoringMaintenanceTaskMapper = monitoringMaintenanceTaskMapper;
    }

    /**
     * Return a {@link Page} of {@link MonitoringMaintenanceTaskDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MonitoringMaintenanceTaskDTO> findByCriteria(MonitoringMaintenanceTaskCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MonitoringMaintenanceTask> specification = createSpecification(criteria);
        return monitoringMaintenanceTaskRepository.findAll(specification, page).map(monitoringMaintenanceTaskMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MonitoringMaintenanceTaskCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<MonitoringMaintenanceTask> specification = createSpecification(criteria);
        return monitoringMaintenanceTaskRepository.count(specification);
    }

    /**
     * Function to convert {@link MonitoringMaintenanceTaskCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MonitoringMaintenanceTask> createSpecification(MonitoringMaintenanceTaskCriteria criteria) {
        Specification<MonitoringMaintenanceTask> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), MonitoringMaintenanceTask_.id),
                buildStringSpecification(criteria.getName(), MonitoringMaintenanceTask_.name),
                buildSpecification(criteria.getTaskType(), MonitoringMaintenanceTask_.taskType),
                buildStringSpecification(criteria.getSchedule(), MonitoringMaintenanceTask_.schedule),
                buildSpecification(criteria.getStatus(), MonitoringMaintenanceTask_.status),
                buildSpecification(criteria.getIsActive(), MonitoringMaintenanceTask_.isActive),
                buildRangeSpecification(criteria.getLastRun(), MonitoringMaintenanceTask_.lastRun),
                buildRangeSpecification(criteria.getNextRun(), MonitoringMaintenanceTask_.nextRun),
                buildRangeSpecification(criteria.getDuration(), MonitoringMaintenanceTask_.duration),
                buildRangeSpecification(criteria.getRecordsProcessed(), MonitoringMaintenanceTask_.recordsProcessed),
                buildStringSpecification(criteria.getCreatedBy(), MonitoringMaintenanceTask_.createdBy),
                buildRangeSpecification(criteria.getCreatedDate(), MonitoringMaintenanceTask_.createdDate)
            );
        }
        return specification;
    }
}
