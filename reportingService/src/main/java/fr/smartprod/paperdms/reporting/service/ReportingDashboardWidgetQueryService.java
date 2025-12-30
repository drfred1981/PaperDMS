package fr.smartprod.paperdms.reporting.service;

import fr.smartprod.paperdms.reporting.domain.*; // for static metamodels
import fr.smartprod.paperdms.reporting.domain.ReportingDashboardWidget;
import fr.smartprod.paperdms.reporting.repository.ReportingDashboardWidgetRepository;
import fr.smartprod.paperdms.reporting.service.criteria.ReportingDashboardWidgetCriteria;
import fr.smartprod.paperdms.reporting.service.dto.ReportingDashboardWidgetDTO;
import fr.smartprod.paperdms.reporting.service.mapper.ReportingDashboardWidgetMapper;
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
 * Service for executing complex queries for {@link ReportingDashboardWidget} entities in the database.
 * The main input is a {@link ReportingDashboardWidgetCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ReportingDashboardWidgetDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ReportingDashboardWidgetQueryService extends QueryService<ReportingDashboardWidget> {

    private static final Logger LOG = LoggerFactory.getLogger(ReportingDashboardWidgetQueryService.class);

    private final ReportingDashboardWidgetRepository reportingDashboardWidgetRepository;

    private final ReportingDashboardWidgetMapper reportingDashboardWidgetMapper;

    public ReportingDashboardWidgetQueryService(
        ReportingDashboardWidgetRepository reportingDashboardWidgetRepository,
        ReportingDashboardWidgetMapper reportingDashboardWidgetMapper
    ) {
        this.reportingDashboardWidgetRepository = reportingDashboardWidgetRepository;
        this.reportingDashboardWidgetMapper = reportingDashboardWidgetMapper;
    }

    /**
     * Return a {@link Page} of {@link ReportingDashboardWidgetDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ReportingDashboardWidgetDTO> findByCriteria(ReportingDashboardWidgetCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ReportingDashboardWidget> specification = createSpecification(criteria);
        return reportingDashboardWidgetRepository.findAll(specification, page).map(reportingDashboardWidgetMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ReportingDashboardWidgetCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ReportingDashboardWidget> specification = createSpecification(criteria);
        return reportingDashboardWidgetRepository.count(specification);
    }

    /**
     * Function to convert {@link ReportingDashboardWidgetCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ReportingDashboardWidget> createSpecification(ReportingDashboardWidgetCriteria criteria) {
        Specification<ReportingDashboardWidget> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), ReportingDashboardWidget_.id),
                buildSpecification(criteria.getWidgetType(), ReportingDashboardWidget_.widgetType),
                buildStringSpecification(criteria.getTitle(), ReportingDashboardWidget_.title),
                buildStringSpecification(criteria.getDataSource(), ReportingDashboardWidget_.dataSource),
                buildRangeSpecification(criteria.getPosition(), ReportingDashboardWidget_.position),
                buildRangeSpecification(criteria.getSizeX(), ReportingDashboardWidget_.sizeX),
                buildRangeSpecification(criteria.getSizeY(), ReportingDashboardWidget_.sizeY),
                buildSpecification(criteria.getDashboarId(), root ->
                    root.join(ReportingDashboardWidget_.dashboar, JoinType.LEFT).get(ReportingDashboard_.id)
                )
            );
        }
        return specification;
    }
}
