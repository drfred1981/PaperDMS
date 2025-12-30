package fr.smartprod.paperdms.monitoring.service;

import fr.smartprod.paperdms.monitoring.domain.*; // for static metamodels
import fr.smartprod.paperdms.monitoring.domain.MonitoringAlertRule;
import fr.smartprod.paperdms.monitoring.repository.MonitoringAlertRuleRepository;
import fr.smartprod.paperdms.monitoring.service.criteria.MonitoringAlertRuleCriteria;
import fr.smartprod.paperdms.monitoring.service.dto.MonitoringAlertRuleDTO;
import fr.smartprod.paperdms.monitoring.service.mapper.MonitoringAlertRuleMapper;
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
 * Service for executing complex queries for {@link MonitoringAlertRule} entities in the database.
 * The main input is a {@link MonitoringAlertRuleCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link MonitoringAlertRuleDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MonitoringAlertRuleQueryService extends QueryService<MonitoringAlertRule> {

    private static final Logger LOG = LoggerFactory.getLogger(MonitoringAlertRuleQueryService.class);

    private final MonitoringAlertRuleRepository monitoringAlertRuleRepository;

    private final MonitoringAlertRuleMapper monitoringAlertRuleMapper;

    public MonitoringAlertRuleQueryService(
        MonitoringAlertRuleRepository monitoringAlertRuleRepository,
        MonitoringAlertRuleMapper monitoringAlertRuleMapper
    ) {
        this.monitoringAlertRuleRepository = monitoringAlertRuleRepository;
        this.monitoringAlertRuleMapper = monitoringAlertRuleMapper;
    }

    /**
     * Return a {@link Page} of {@link MonitoringAlertRuleDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MonitoringAlertRuleDTO> findByCriteria(MonitoringAlertRuleCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MonitoringAlertRule> specification = createSpecification(criteria);
        return monitoringAlertRuleRepository.findAll(specification, page).map(monitoringAlertRuleMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MonitoringAlertRuleCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<MonitoringAlertRule> specification = createSpecification(criteria);
        return monitoringAlertRuleRepository.count(specification);
    }

    /**
     * Function to convert {@link MonitoringAlertRuleCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MonitoringAlertRule> createSpecification(MonitoringAlertRuleCriteria criteria) {
        Specification<MonitoringAlertRule> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), MonitoringAlertRule_.id),
                buildStringSpecification(criteria.getName(), MonitoringAlertRule_.name),
                buildSpecification(criteria.getAlertType(), MonitoringAlertRule_.alertType),
                buildSpecification(criteria.getSeverity(), MonitoringAlertRule_.severity),
                buildSpecification(criteria.getIsActive(), MonitoringAlertRule_.isActive),
                buildRangeSpecification(criteria.getTriggerCount(), MonitoringAlertRule_.triggerCount),
                buildRangeSpecification(criteria.getLastTriggered(), MonitoringAlertRule_.lastTriggered),
                buildStringSpecification(criteria.getCreatedBy(), MonitoringAlertRule_.createdBy),
                buildRangeSpecification(criteria.getCreatedDate(), MonitoringAlertRule_.createdDate),
                buildSpecification(criteria.getAlertsId(), root ->
                    root.join(MonitoringAlertRule_.alerts, JoinType.LEFT).get(MonitoringAlert_.id)
                )
            );
        }
        return specification;
    }
}
