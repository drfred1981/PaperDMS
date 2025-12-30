package fr.smartprod.paperdms.monitoring.service;

import fr.smartprod.paperdms.monitoring.domain.*; // for static metamodels
import fr.smartprod.paperdms.monitoring.domain.MonitoringDocumentWatch;
import fr.smartprod.paperdms.monitoring.repository.MonitoringDocumentWatchRepository;
import fr.smartprod.paperdms.monitoring.service.criteria.MonitoringDocumentWatchCriteria;
import fr.smartprod.paperdms.monitoring.service.dto.MonitoringDocumentWatchDTO;
import fr.smartprod.paperdms.monitoring.service.mapper.MonitoringDocumentWatchMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link MonitoringDocumentWatch} entities in the database.
 * The main input is a {@link MonitoringDocumentWatchCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link MonitoringDocumentWatchDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MonitoringDocumentWatchQueryService extends QueryService<MonitoringDocumentWatch> {

    private static final Logger LOG = LoggerFactory.getLogger(MonitoringDocumentWatchQueryService.class);

    private final MonitoringDocumentWatchRepository monitoringDocumentWatchRepository;

    private final MonitoringDocumentWatchMapper monitoringDocumentWatchMapper;

    public MonitoringDocumentWatchQueryService(
        MonitoringDocumentWatchRepository monitoringDocumentWatchRepository,
        MonitoringDocumentWatchMapper monitoringDocumentWatchMapper
    ) {
        this.monitoringDocumentWatchRepository = monitoringDocumentWatchRepository;
        this.monitoringDocumentWatchMapper = monitoringDocumentWatchMapper;
    }

    /**
     * Return a {@link Page} of {@link MonitoringDocumentWatchDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MonitoringDocumentWatchDTO> findByCriteria(MonitoringDocumentWatchCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MonitoringDocumentWatch> specification = createSpecification(criteria);
        return monitoringDocumentWatchRepository.findAll(specification, page).map(monitoringDocumentWatchMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MonitoringDocumentWatchCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<MonitoringDocumentWatch> specification = createSpecification(criteria);
        return monitoringDocumentWatchRepository.count(specification);
    }

    /**
     * Function to convert {@link MonitoringDocumentWatchCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MonitoringDocumentWatch> createSpecification(MonitoringDocumentWatchCriteria criteria) {
        Specification<MonitoringDocumentWatch> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), MonitoringDocumentWatch_.id),
                buildStringSpecification(criteria.getDocumentSha256(), MonitoringDocumentWatch_.documentSha256),
                buildStringSpecification(criteria.getUserId(), MonitoringDocumentWatch_.userId),
                buildSpecification(criteria.getWatchType(), MonitoringDocumentWatch_.watchType),
                buildSpecification(criteria.getNotifyOnView(), MonitoringDocumentWatch_.notifyOnView),
                buildSpecification(criteria.getNotifyOnDownload(), MonitoringDocumentWatch_.notifyOnDownload),
                buildSpecification(criteria.getNotifyOnModify(), MonitoringDocumentWatch_.notifyOnModify),
                buildSpecification(criteria.getNotifyOnShare(), MonitoringDocumentWatch_.notifyOnShare),
                buildSpecification(criteria.getNotifyOnDelete(), MonitoringDocumentWatch_.notifyOnDelete),
                buildRangeSpecification(criteria.getCreatedDate(), MonitoringDocumentWatch_.createdDate)
            );
        }
        return specification;
    }
}
