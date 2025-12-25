package fr.smartprod.paperdms.reporting.service;

import fr.smartprod.paperdms.reporting.domain.*; // for static metamodels
import fr.smartprod.paperdms.reporting.domain.ScheduledReport;
import fr.smartprod.paperdms.reporting.repository.ScheduledReportRepository;
import fr.smartprod.paperdms.reporting.service.criteria.ScheduledReportCriteria;
import fr.smartprod.paperdms.reporting.service.dto.ScheduledReportDTO;
import fr.smartprod.paperdms.reporting.service.mapper.ScheduledReportMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link ScheduledReport} entities in the database.
 * The main input is a {@link ScheduledReportCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ScheduledReportDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ScheduledReportQueryService extends QueryService<ScheduledReport> {

    private static final Logger LOG = LoggerFactory.getLogger(ScheduledReportQueryService.class);

    private final ScheduledReportRepository scheduledReportRepository;

    private final ScheduledReportMapper scheduledReportMapper;

    public ScheduledReportQueryService(ScheduledReportRepository scheduledReportRepository, ScheduledReportMapper scheduledReportMapper) {
        this.scheduledReportRepository = scheduledReportRepository;
        this.scheduledReportMapper = scheduledReportMapper;
    }

    /**
     * Return a {@link Page} of {@link ScheduledReportDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ScheduledReportDTO> findByCriteria(ScheduledReportCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ScheduledReport> specification = createSpecification(criteria);
        return scheduledReportRepository.findAll(specification, page).map(scheduledReportMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ScheduledReportCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ScheduledReport> specification = createSpecification(criteria);
        return scheduledReportRepository.count(specification);
    }

    /**
     * Function to convert {@link ScheduledReportCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ScheduledReport> createSpecification(ScheduledReportCriteria criteria) {
        Specification<ScheduledReport> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), ScheduledReport_.id),
                buildStringSpecification(criteria.getName(), ScheduledReport_.name),
                buildSpecification(criteria.getReportType(), ScheduledReport_.reportType),
                buildStringSpecification(criteria.getSchedule(), ScheduledReport_.schedule),
                buildSpecification(criteria.getFormat(), ScheduledReport_.format),
                buildSpecification(criteria.getIsActive(), ScheduledReport_.isActive),
                buildRangeSpecification(criteria.getLastRun(), ScheduledReport_.lastRun),
                buildRangeSpecification(criteria.getNextRun(), ScheduledReport_.nextRun),
                buildStringSpecification(criteria.getCreatedBy(), ScheduledReport_.createdBy),
                buildRangeSpecification(criteria.getCreatedDate(), ScheduledReport_.createdDate)
            );
        }
        return specification;
    }
}
