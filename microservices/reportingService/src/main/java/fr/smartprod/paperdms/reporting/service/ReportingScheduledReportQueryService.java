package fr.smartprod.paperdms.reporting.service;

import fr.smartprod.paperdms.reporting.domain.*; // for static metamodels
import fr.smartprod.paperdms.reporting.domain.ReportingScheduledReport;
import fr.smartprod.paperdms.reporting.repository.ReportingScheduledReportRepository;
import fr.smartprod.paperdms.reporting.service.criteria.ReportingScheduledReportCriteria;
import fr.smartprod.paperdms.reporting.service.dto.ReportingScheduledReportDTO;
import fr.smartprod.paperdms.reporting.service.mapper.ReportingScheduledReportMapper;
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
 * Service for executing complex queries for {@link ReportingScheduledReport} entities in the database.
 * The main input is a {@link ReportingScheduledReportCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ReportingScheduledReportDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ReportingScheduledReportQueryService extends QueryService<ReportingScheduledReport> {

    private static final Logger LOG = LoggerFactory.getLogger(ReportingScheduledReportQueryService.class);

    private final ReportingScheduledReportRepository reportingScheduledReportRepository;

    private final ReportingScheduledReportMapper reportingScheduledReportMapper;

    public ReportingScheduledReportQueryService(
        ReportingScheduledReportRepository reportingScheduledReportRepository,
        ReportingScheduledReportMapper reportingScheduledReportMapper
    ) {
        this.reportingScheduledReportRepository = reportingScheduledReportRepository;
        this.reportingScheduledReportMapper = reportingScheduledReportMapper;
    }

    /**
     * Return a {@link Page} of {@link ReportingScheduledReportDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ReportingScheduledReportDTO> findByCriteria(ReportingScheduledReportCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ReportingScheduledReport> specification = createSpecification(criteria);
        return reportingScheduledReportRepository.findAll(specification, page).map(reportingScheduledReportMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ReportingScheduledReportCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ReportingScheduledReport> specification = createSpecification(criteria);
        return reportingScheduledReportRepository.count(specification);
    }

    /**
     * Function to convert {@link ReportingScheduledReportCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ReportingScheduledReport> createSpecification(ReportingScheduledReportCriteria criteria) {
        Specification<ReportingScheduledReport> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), ReportingScheduledReport_.id),
                buildStringSpecification(criteria.getName(), ReportingScheduledReport_.name),
                buildSpecification(criteria.getReportType(), ReportingScheduledReport_.reportType),
                buildStringSpecification(criteria.getSchedule(), ReportingScheduledReport_.schedule),
                buildSpecification(criteria.getFormat(), ReportingScheduledReport_.format),
                buildSpecification(criteria.getIsActive(), ReportingScheduledReport_.isActive),
                buildRangeSpecification(criteria.getLastRun(), ReportingScheduledReport_.lastRun),
                buildRangeSpecification(criteria.getNextRun(), ReportingScheduledReport_.nextRun),
                buildStringSpecification(criteria.getCreatedBy(), ReportingScheduledReport_.createdBy),
                buildRangeSpecification(criteria.getCreatedDate(), ReportingScheduledReport_.createdDate),
                buildSpecification(criteria.getReportsExecutionsId(), root ->
                    root.join(ReportingScheduledReport_.reportsExecutions, JoinType.LEFT).get(ReportingExecution_.id)
                )
            );
        }
        return specification;
    }
}
