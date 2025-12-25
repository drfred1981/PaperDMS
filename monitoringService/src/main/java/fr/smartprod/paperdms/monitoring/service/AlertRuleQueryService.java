package fr.smartprod.paperdms.monitoring.service;

import fr.smartprod.paperdms.monitoring.domain.*; // for static metamodels
import fr.smartprod.paperdms.monitoring.domain.AlertRule;
import fr.smartprod.paperdms.monitoring.repository.AlertRuleRepository;
import fr.smartprod.paperdms.monitoring.service.criteria.AlertRuleCriteria;
import fr.smartprod.paperdms.monitoring.service.dto.AlertRuleDTO;
import fr.smartprod.paperdms.monitoring.service.mapper.AlertRuleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link AlertRule} entities in the database.
 * The main input is a {@link AlertRuleCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AlertRuleDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AlertRuleQueryService extends QueryService<AlertRule> {

    private static final Logger LOG = LoggerFactory.getLogger(AlertRuleQueryService.class);

    private final AlertRuleRepository alertRuleRepository;

    private final AlertRuleMapper alertRuleMapper;

    public AlertRuleQueryService(AlertRuleRepository alertRuleRepository, AlertRuleMapper alertRuleMapper) {
        this.alertRuleRepository = alertRuleRepository;
        this.alertRuleMapper = alertRuleMapper;
    }

    /**
     * Return a {@link Page} of {@link AlertRuleDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AlertRuleDTO> findByCriteria(AlertRuleCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AlertRule> specification = createSpecification(criteria);
        return alertRuleRepository.findAll(specification, page).map(alertRuleMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AlertRuleCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AlertRule> specification = createSpecification(criteria);
        return alertRuleRepository.count(specification);
    }

    /**
     * Function to convert {@link AlertRuleCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AlertRule> createSpecification(AlertRuleCriteria criteria) {
        Specification<AlertRule> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), AlertRule_.id),
                buildStringSpecification(criteria.getName(), AlertRule_.name),
                buildSpecification(criteria.getAlertType(), AlertRule_.alertType),
                buildSpecification(criteria.getSeverity(), AlertRule_.severity),
                buildSpecification(criteria.getIsActive(), AlertRule_.isActive),
                buildRangeSpecification(criteria.getTriggerCount(), AlertRule_.triggerCount),
                buildRangeSpecification(criteria.getLastTriggered(), AlertRule_.lastTriggered),
                buildStringSpecification(criteria.getCreatedBy(), AlertRule_.createdBy),
                buildRangeSpecification(criteria.getCreatedDate(), AlertRule_.createdDate)
            );
        }
        return specification;
    }
}
