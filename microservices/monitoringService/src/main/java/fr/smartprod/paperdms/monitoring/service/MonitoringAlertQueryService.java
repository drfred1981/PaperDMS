package fr.smartprod.paperdms.monitoring.service;

import fr.smartprod.paperdms.monitoring.domain.*; // for static metamodels
import fr.smartprod.paperdms.monitoring.domain.MonitoringAlert;
import fr.smartprod.paperdms.monitoring.repository.MonitoringAlertRepository;
import fr.smartprod.paperdms.monitoring.service.criteria.MonitoringAlertCriteria;
import fr.smartprod.paperdms.monitoring.service.dto.MonitoringAlertDTO;
import fr.smartprod.paperdms.monitoring.service.mapper.MonitoringAlertMapper;
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
 * Service for executing complex queries for {@link MonitoringAlert} entities in the database.
 * The main input is a {@link MonitoringAlertCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link MonitoringAlertDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MonitoringAlertQueryService extends QueryService<MonitoringAlert> {

    private static final Logger LOG = LoggerFactory.getLogger(MonitoringAlertQueryService.class);

    private final MonitoringAlertRepository monitoringAlertRepository;

    private final MonitoringAlertMapper monitoringAlertMapper;

    public MonitoringAlertQueryService(MonitoringAlertRepository monitoringAlertRepository, MonitoringAlertMapper monitoringAlertMapper) {
        this.monitoringAlertRepository = monitoringAlertRepository;
        this.monitoringAlertMapper = monitoringAlertMapper;
    }

    /**
     * Return a {@link Page} of {@link MonitoringAlertDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MonitoringAlertDTO> findByCriteria(MonitoringAlertCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MonitoringAlert> specification = createSpecification(criteria);
        return monitoringAlertRepository.findAll(specification, page).map(monitoringAlertMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MonitoringAlertCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<MonitoringAlert> specification = createSpecification(criteria);
        return monitoringAlertRepository.count(specification);
    }

    /**
     * Function to convert {@link MonitoringAlertCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MonitoringAlert> createSpecification(MonitoringAlertCriteria criteria) {
        Specification<MonitoringAlert> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), MonitoringAlert_.id),
                buildSpecification(criteria.getSeverity(), MonitoringAlert_.severity),
                buildStringSpecification(criteria.getTitle(), MonitoringAlert_.title),
                buildStringSpecification(criteria.getEntityType(), MonitoringAlert_.entityType),
                buildStringSpecification(criteria.getEntityName(), MonitoringAlert_.entityName),
                buildSpecification(criteria.getStatus(), MonitoringAlert_.status),
                buildRangeSpecification(criteria.getTriggeredDate(), MonitoringAlert_.triggeredDate),
                buildStringSpecification(criteria.getAcknowledgedBy(), MonitoringAlert_.acknowledgedBy),
                buildRangeSpecification(criteria.getAcknowledgedDate(), MonitoringAlert_.acknowledgedDate),
                buildStringSpecification(criteria.getResolvedBy(), MonitoringAlert_.resolvedBy),
                buildRangeSpecification(criteria.getResolvedDate(), MonitoringAlert_.resolvedDate),
                buildSpecification(criteria.getAlertRuleId(), root ->
                    root.join(MonitoringAlert_.alertRule, JoinType.LEFT).get(MonitoringAlertRule_.id)
                )
            );
        }
        return specification;
    }
}
