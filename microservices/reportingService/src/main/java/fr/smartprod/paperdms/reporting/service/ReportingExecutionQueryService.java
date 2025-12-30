package fr.smartprod.paperdms.reporting.service;

import fr.smartprod.paperdms.reporting.domain.*; // for static metamodels
import fr.smartprod.paperdms.reporting.domain.ReportingExecution;
import fr.smartprod.paperdms.reporting.repository.ReportingExecutionRepository;
import fr.smartprod.paperdms.reporting.service.criteria.ReportingExecutionCriteria;
import fr.smartprod.paperdms.reporting.service.dto.ReportingExecutionDTO;
import fr.smartprod.paperdms.reporting.service.mapper.ReportingExecutionMapper;
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
 * Service for executing complex queries for {@link ReportingExecution} entities in the database.
 * The main input is a {@link ReportingExecutionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ReportingExecutionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ReportingExecutionQueryService extends QueryService<ReportingExecution> {

    private static final Logger LOG = LoggerFactory.getLogger(ReportingExecutionQueryService.class);

    private final ReportingExecutionRepository reportingExecutionRepository;

    private final ReportingExecutionMapper reportingExecutionMapper;

    public ReportingExecutionQueryService(
        ReportingExecutionRepository reportingExecutionRepository,
        ReportingExecutionMapper reportingExecutionMapper
    ) {
        this.reportingExecutionRepository = reportingExecutionRepository;
        this.reportingExecutionMapper = reportingExecutionMapper;
    }

    /**
     * Return a {@link Page} of {@link ReportingExecutionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ReportingExecutionDTO> findByCriteria(ReportingExecutionCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ReportingExecution> specification = createSpecification(criteria);
        return reportingExecutionRepository.findAll(specification, page).map(reportingExecutionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ReportingExecutionCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ReportingExecution> specification = createSpecification(criteria);
        return reportingExecutionRepository.count(specification);
    }

    /**
     * Function to convert {@link ReportingExecutionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ReportingExecution> createSpecification(ReportingExecutionCriteria criteria) {
        Specification<ReportingExecution> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), ReportingExecution_.id),
                buildSpecification(criteria.getStatus(), ReportingExecution_.status),
                buildRangeSpecification(criteria.getStartDate(), ReportingExecution_.startDate),
                buildRangeSpecification(criteria.getEndDate(), ReportingExecution_.endDate),
                buildRangeSpecification(criteria.getRecordsProcessed(), ReportingExecution_.recordsProcessed),
                buildStringSpecification(criteria.getOutputS3Key(), ReportingExecution_.outputS3Key),
                buildRangeSpecification(criteria.getOutputSize(), ReportingExecution_.outputSize),
                buildSpecification(criteria.getScheduledReportId(), root ->
                    root.join(ReportingExecution_.scheduledReport, JoinType.LEFT).get(ReportingScheduledReport_.id)
                )
            );
        }
        return specification;
    }
}
