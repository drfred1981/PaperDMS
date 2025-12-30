package fr.smartprod.paperdms.monitoring.service;

import fr.smartprod.paperdms.monitoring.domain.*; // for static metamodels
import fr.smartprod.paperdms.monitoring.domain.MonitoringSystemHealth;
import fr.smartprod.paperdms.monitoring.repository.MonitoringSystemHealthRepository;
import fr.smartprod.paperdms.monitoring.service.criteria.MonitoringSystemHealthCriteria;
import fr.smartprod.paperdms.monitoring.service.dto.MonitoringSystemHealthDTO;
import fr.smartprod.paperdms.monitoring.service.mapper.MonitoringSystemHealthMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link MonitoringSystemHealth} entities in the database.
 * The main input is a {@link MonitoringSystemHealthCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link MonitoringSystemHealthDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MonitoringSystemHealthQueryService extends QueryService<MonitoringSystemHealth> {

    private static final Logger LOG = LoggerFactory.getLogger(MonitoringSystemHealthQueryService.class);

    private final MonitoringSystemHealthRepository monitoringSystemHealthRepository;

    private final MonitoringSystemHealthMapper monitoringSystemHealthMapper;

    public MonitoringSystemHealthQueryService(
        MonitoringSystemHealthRepository monitoringSystemHealthRepository,
        MonitoringSystemHealthMapper monitoringSystemHealthMapper
    ) {
        this.monitoringSystemHealthRepository = monitoringSystemHealthRepository;
        this.monitoringSystemHealthMapper = monitoringSystemHealthMapper;
    }

    /**
     * Return a {@link Page} of {@link MonitoringSystemHealthDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MonitoringSystemHealthDTO> findByCriteria(MonitoringSystemHealthCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MonitoringSystemHealth> specification = createSpecification(criteria);
        return monitoringSystemHealthRepository.findAll(specification, page).map(monitoringSystemHealthMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MonitoringSystemHealthCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<MonitoringSystemHealth> specification = createSpecification(criteria);
        return monitoringSystemHealthRepository.count(specification);
    }

    /**
     * Function to convert {@link MonitoringSystemHealthCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MonitoringSystemHealth> createSpecification(MonitoringSystemHealthCriteria criteria) {
        Specification<MonitoringSystemHealth> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), MonitoringSystemHealth_.id),
                buildStringSpecification(criteria.getServiceName(), MonitoringSystemHealth_.serviceName),
                buildSpecification(criteria.getStatus(), MonitoringSystemHealth_.status),
                buildStringSpecification(criteria.getVersion(), MonitoringSystemHealth_.version),
                buildRangeSpecification(criteria.getUptime(), MonitoringSystemHealth_.uptime),
                buildRangeSpecification(criteria.getCpuUsage(), MonitoringSystemHealth_.cpuUsage),
                buildRangeSpecification(criteria.getMemoryUsage(), MonitoringSystemHealth_.memoryUsage),
                buildRangeSpecification(criteria.getErrorRate(), MonitoringSystemHealth_.errorRate),
                buildRangeSpecification(criteria.getLastCheck(), MonitoringSystemHealth_.lastCheck)
            );
        }
        return specification;
    }
}
