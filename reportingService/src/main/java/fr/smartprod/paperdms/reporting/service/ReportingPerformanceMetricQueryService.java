package fr.smartprod.paperdms.reporting.service;

import fr.smartprod.paperdms.reporting.domain.*; // for static metamodels
import fr.smartprod.paperdms.reporting.domain.ReportingPerformanceMetric;
import fr.smartprod.paperdms.reporting.repository.ReportingPerformanceMetricRepository;
import fr.smartprod.paperdms.reporting.service.criteria.ReportingPerformanceMetricCriteria;
import fr.smartprod.paperdms.reporting.service.dto.ReportingPerformanceMetricDTO;
import fr.smartprod.paperdms.reporting.service.mapper.ReportingPerformanceMetricMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link ReportingPerformanceMetric} entities in the database.
 * The main input is a {@link ReportingPerformanceMetricCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ReportingPerformanceMetricDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ReportingPerformanceMetricQueryService extends QueryService<ReportingPerformanceMetric> {

    private static final Logger LOG = LoggerFactory.getLogger(ReportingPerformanceMetricQueryService.class);

    private final ReportingPerformanceMetricRepository reportingPerformanceMetricRepository;

    private final ReportingPerformanceMetricMapper reportingPerformanceMetricMapper;

    public ReportingPerformanceMetricQueryService(
        ReportingPerformanceMetricRepository reportingPerformanceMetricRepository,
        ReportingPerformanceMetricMapper reportingPerformanceMetricMapper
    ) {
        this.reportingPerformanceMetricRepository = reportingPerformanceMetricRepository;
        this.reportingPerformanceMetricMapper = reportingPerformanceMetricMapper;
    }

    /**
     * Return a {@link Page} of {@link ReportingPerformanceMetricDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ReportingPerformanceMetricDTO> findByCriteria(ReportingPerformanceMetricCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ReportingPerformanceMetric> specification = createSpecification(criteria);
        return reportingPerformanceMetricRepository.findAll(specification, page).map(reportingPerformanceMetricMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ReportingPerformanceMetricCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ReportingPerformanceMetric> specification = createSpecification(criteria);
        return reportingPerformanceMetricRepository.count(specification);
    }

    /**
     * Function to convert {@link ReportingPerformanceMetricCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ReportingPerformanceMetric> createSpecification(ReportingPerformanceMetricCriteria criteria) {
        Specification<ReportingPerformanceMetric> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), ReportingPerformanceMetric_.id),
                buildStringSpecification(criteria.getMetricName(), ReportingPerformanceMetric_.metricName),
                buildSpecification(criteria.getMetricType(), ReportingPerformanceMetric_.metricType),
                buildRangeSpecification(criteria.getValue(), ReportingPerformanceMetric_.value),
                buildStringSpecification(criteria.getUnit(), ReportingPerformanceMetric_.unit),
                buildStringSpecification(criteria.getServiceName(), ReportingPerformanceMetric_.serviceName),
                buildRangeSpecification(criteria.getTimestamp(), ReportingPerformanceMetric_.timestamp)
            );
        }
        return specification;
    }
}
