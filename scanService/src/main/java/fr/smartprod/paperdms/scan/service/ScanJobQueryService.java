package fr.smartprod.paperdms.scan.service;

import fr.smartprod.paperdms.scan.domain.*; // for static metamodels
import fr.smartprod.paperdms.scan.domain.ScanJob;
import fr.smartprod.paperdms.scan.repository.ScanJobRepository;
import fr.smartprod.paperdms.scan.service.criteria.ScanJobCriteria;
import fr.smartprod.paperdms.scan.service.dto.ScanJobDTO;
import fr.smartprod.paperdms.scan.service.mapper.ScanJobMapper;
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
 * Service for executing complex queries for {@link ScanJob} entities in the database.
 * The main input is a {@link ScanJobCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ScanJobDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ScanJobQueryService extends QueryService<ScanJob> {

    private static final Logger LOG = LoggerFactory.getLogger(ScanJobQueryService.class);

    private final ScanJobRepository scanJobRepository;

    private final ScanJobMapper scanJobMapper;

    public ScanJobQueryService(ScanJobRepository scanJobRepository, ScanJobMapper scanJobMapper) {
        this.scanJobRepository = scanJobRepository;
        this.scanJobMapper = scanJobMapper;
    }

    /**
     * Return a {@link Page} of {@link ScanJobDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ScanJobDTO> findByCriteria(ScanJobCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ScanJob> specification = createSpecification(criteria);
        return scanJobRepository.findAll(specification, page).map(scanJobMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ScanJobCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ScanJob> specification = createSpecification(criteria);
        return scanJobRepository.count(specification);
    }

    /**
     * Function to convert {@link ScanJobCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ScanJob> createSpecification(ScanJobCriteria criteria) {
        Specification<ScanJob> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), ScanJob_.id),
                buildStringSpecification(criteria.getName(), ScanJob_.name),
                buildRangeSpecification(criteria.getScannerConfigId(), ScanJob_.scannerConfigId),
                buildRangeSpecification(criteria.getBatchId(), ScanJob_.batchId),
                buildRangeSpecification(criteria.getDocumentTypeId(), ScanJob_.documentTypeId),
                buildRangeSpecification(criteria.getFolderId(), ScanJob_.folderId),
                buildRangeSpecification(criteria.getPageCount(), ScanJob_.pageCount),
                buildSpecification(criteria.getStatus(), ScanJob_.status),
                buildSpecification(criteria.getColorMode(), ScanJob_.colorMode),
                buildRangeSpecification(criteria.getResolution(), ScanJob_.resolution),
                buildSpecification(criteria.getFileFormat(), ScanJob_.fileFormat),
                buildRangeSpecification(criteria.getStartDate(), ScanJob_.startDate),
                buildRangeSpecification(criteria.getEndDate(), ScanJob_.endDate),
                buildStringSpecification(criteria.getCreatedBy(), ScanJob_.createdBy),
                buildRangeSpecification(criteria.getCreatedDate(), ScanJob_.createdDate),
                buildSpecification(criteria.getScannerConfigId(), root ->
                    root.join(ScanJob_.scannerConfig, JoinType.LEFT).get(ScannerConfiguration_.id)
                ),
                buildSpecification(criteria.getBatchId(), root -> root.join(ScanJob_.batch, JoinType.LEFT).get(ScanBatch_.id))
            );
        }
        return specification;
    }
}
