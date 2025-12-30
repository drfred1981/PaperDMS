package fr.smartprod.paperdms.monitoring.service;

import fr.smartprod.paperdms.monitoring.domain.*; // for static metamodels
import fr.smartprod.paperdms.monitoring.domain.MonitoringServiceStatus;
import fr.smartprod.paperdms.monitoring.repository.MonitoringServiceStatusRepository;
import fr.smartprod.paperdms.monitoring.service.criteria.MonitoringServiceStatusCriteria;
import fr.smartprod.paperdms.monitoring.service.dto.MonitoringServiceStatusDTO;
import fr.smartprod.paperdms.monitoring.service.mapper.MonitoringServiceStatusMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link MonitoringServiceStatus} entities in the database.
 * The main input is a {@link MonitoringServiceStatusCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link MonitoringServiceStatusDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MonitoringServiceStatusQueryService extends QueryService<MonitoringServiceStatus> {

    private static final Logger LOG = LoggerFactory.getLogger(MonitoringServiceStatusQueryService.class);

    private final MonitoringServiceStatusRepository monitoringServiceStatusRepository;

    private final MonitoringServiceStatusMapper monitoringServiceStatusMapper;

    public MonitoringServiceStatusQueryService(
        MonitoringServiceStatusRepository monitoringServiceStatusRepository,
        MonitoringServiceStatusMapper monitoringServiceStatusMapper
    ) {
        this.monitoringServiceStatusRepository = monitoringServiceStatusRepository;
        this.monitoringServiceStatusMapper = monitoringServiceStatusMapper;
    }

    /**
     * Return a {@link Page} of {@link MonitoringServiceStatusDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MonitoringServiceStatusDTO> findByCriteria(MonitoringServiceStatusCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MonitoringServiceStatus> specification = createSpecification(criteria);
        return monitoringServiceStatusRepository.findAll(specification, page).map(monitoringServiceStatusMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MonitoringServiceStatusCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<MonitoringServiceStatus> specification = createSpecification(criteria);
        return monitoringServiceStatusRepository.count(specification);
    }

    /**
     * Function to convert {@link MonitoringServiceStatusCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MonitoringServiceStatus> createSpecification(MonitoringServiceStatusCriteria criteria) {
        Specification<MonitoringServiceStatus> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), MonitoringServiceStatus_.id),
                buildStringSpecification(criteria.getServiceName(), MonitoringServiceStatus_.serviceName),
                buildStringSpecification(criteria.getServiceType(), MonitoringServiceStatus_.serviceType),
                buildSpecification(criteria.getStatus(), MonitoringServiceStatus_.status),
                buildStringSpecification(criteria.getEndpoint(), MonitoringServiceStatus_.endpoint),
                buildRangeSpecification(criteria.getPort(), MonitoringServiceStatus_.port),
                buildStringSpecification(criteria.getVersion(), MonitoringServiceStatus_.version),
                buildRangeSpecification(criteria.getLastPing(), MonitoringServiceStatus_.lastPing),
                buildSpecification(criteria.getIsHealthy(), MonitoringServiceStatus_.isHealthy)
            );
        }
        return specification;
    }
}
