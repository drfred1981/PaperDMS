package fr.smartprod.paperdms.scan.service;

import fr.smartprod.paperdms.scan.domain.*; // for static metamodels
import fr.smartprod.paperdms.scan.domain.ScanBatch;
import fr.smartprod.paperdms.scan.repository.ScanBatchRepository;
import fr.smartprod.paperdms.scan.service.criteria.ScanBatchCriteria;
import fr.smartprod.paperdms.scan.service.dto.ScanBatchDTO;
import fr.smartprod.paperdms.scan.service.mapper.ScanBatchMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link ScanBatch} entities in the database.
 * The main input is a {@link ScanBatchCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ScanBatchDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ScanBatchQueryService extends QueryService<ScanBatch> {

    private static final Logger LOG = LoggerFactory.getLogger(ScanBatchQueryService.class);

    private final ScanBatchRepository scanBatchRepository;

    private final ScanBatchMapper scanBatchMapper;

    public ScanBatchQueryService(ScanBatchRepository scanBatchRepository, ScanBatchMapper scanBatchMapper) {
        this.scanBatchRepository = scanBatchRepository;
        this.scanBatchMapper = scanBatchMapper;
    }

    /**
     * Return a {@link Page} of {@link ScanBatchDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ScanBatchDTO> findByCriteria(ScanBatchCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ScanBatch> specification = createSpecification(criteria);
        return scanBatchRepository.findAll(specification, page).map(scanBatchMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ScanBatchCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ScanBatch> specification = createSpecification(criteria);
        return scanBatchRepository.count(specification);
    }

    /**
     * Function to convert {@link ScanBatchCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ScanBatch> createSpecification(ScanBatchCriteria criteria) {
        Specification<ScanBatch> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), ScanBatch_.id),
                buildStringSpecification(criteria.getName(), ScanBatch_.name),
                buildRangeSpecification(criteria.getTotalJobs(), ScanBatch_.totalJobs),
                buildRangeSpecification(criteria.getCompletedJobs(), ScanBatch_.completedJobs),
                buildRangeSpecification(criteria.getTotalPages(), ScanBatch_.totalPages),
                buildSpecification(criteria.getStatus(), ScanBatch_.status),
                buildStringSpecification(criteria.getCreatedBy(), ScanBatch_.createdBy),
                buildRangeSpecification(criteria.getCreatedDate(), ScanBatch_.createdDate)
            );
        }
        return specification;
    }
}
