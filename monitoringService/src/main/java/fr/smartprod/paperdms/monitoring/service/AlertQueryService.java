package fr.smartprod.paperdms.monitoring.service;

import fr.smartprod.paperdms.monitoring.domain.*; // for static metamodels
import fr.smartprod.paperdms.monitoring.domain.Alert;
import fr.smartprod.paperdms.monitoring.repository.AlertRepository;
import fr.smartprod.paperdms.monitoring.service.criteria.AlertCriteria;
import fr.smartprod.paperdms.monitoring.service.dto.AlertDTO;
import fr.smartprod.paperdms.monitoring.service.mapper.AlertMapper;
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
 * Service for executing complex queries for {@link Alert} entities in the database.
 * The main input is a {@link AlertCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AlertDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AlertQueryService extends QueryService<Alert> {

    private static final Logger LOG = LoggerFactory.getLogger(AlertQueryService.class);

    private final AlertRepository alertRepository;

    private final AlertMapper alertMapper;

    public AlertQueryService(AlertRepository alertRepository, AlertMapper alertMapper) {
        this.alertRepository = alertRepository;
        this.alertMapper = alertMapper;
    }

    /**
     * Return a {@link Page} of {@link AlertDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AlertDTO> findByCriteria(AlertCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Alert> specification = createSpecification(criteria);
        return alertRepository.findAll(specification, page).map(alertMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AlertCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Alert> specification = createSpecification(criteria);
        return alertRepository.count(specification);
    }

    /**
     * Function to convert {@link AlertCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Alert> createSpecification(AlertCriteria criteria) {
        Specification<Alert> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Alert_.id),
                buildRangeSpecification(criteria.getAlertRuleId(), Alert_.alertRuleId),
                buildSpecification(criteria.getSeverity(), Alert_.severity),
                buildStringSpecification(criteria.getTitle(), Alert_.title),
                buildStringSpecification(criteria.getEntityType(), Alert_.entityType),
                buildRangeSpecification(criteria.getEntityId(), Alert_.entityId),
                buildSpecification(criteria.getStatus(), Alert_.status),
                buildRangeSpecification(criteria.getTriggeredDate(), Alert_.triggeredDate),
                buildStringSpecification(criteria.getAcknowledgedBy(), Alert_.acknowledgedBy),
                buildRangeSpecification(criteria.getAcknowledgedDate(), Alert_.acknowledgedDate),
                buildStringSpecification(criteria.getResolvedBy(), Alert_.resolvedBy),
                buildRangeSpecification(criteria.getResolvedDate(), Alert_.resolvedDate),
                buildSpecification(criteria.getAlertRuleId(), root -> root.join(Alert_.alertRule, JoinType.LEFT).get(AlertRule_.id))
            );
        }
        return specification;
    }
}
