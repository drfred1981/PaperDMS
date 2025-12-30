package fr.smartprod.paperdms.export.service;

import fr.smartprod.paperdms.export.domain.*; // for static metamodels
import fr.smartprod.paperdms.export.domain.ExportJob;
import fr.smartprod.paperdms.export.repository.ExportJobRepository;
import fr.smartprod.paperdms.export.service.criteria.ExportJobCriteria;
import fr.smartprod.paperdms.export.service.dto.ExportJobDTO;
import fr.smartprod.paperdms.export.service.mapper.ExportJobMapper;
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
 * Service for executing complex queries for {@link ExportJob} entities in the database.
 * The main input is a {@link ExportJobCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ExportJobDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ExportJobQueryService extends QueryService<ExportJob> {

    private static final Logger LOG = LoggerFactory.getLogger(ExportJobQueryService.class);

    private final ExportJobRepository exportJobRepository;

    private final ExportJobMapper exportJobMapper;

    public ExportJobQueryService(ExportJobRepository exportJobRepository, ExportJobMapper exportJobMapper) {
        this.exportJobRepository = exportJobRepository;
        this.exportJobMapper = exportJobMapper;
    }

    /**
     * Return a {@link Page} of {@link ExportJobDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ExportJobDTO> findByCriteria(ExportJobCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ExportJob> specification = createSpecification(criteria);
        return exportJobRepository.findAll(specification, page).map(exportJobMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ExportJobCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ExportJob> specification = createSpecification(criteria);
        return exportJobRepository.count(specification);
    }

    /**
     * Function to convert {@link ExportJobCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ExportJob> createSpecification(ExportJobCriteria criteria) {
        Specification<ExportJob> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), ExportJob_.id),
                buildStringSpecification(criteria.getName(), ExportJob_.name),
                buildSpecification(criteria.getExportFormat(), ExportJob_.exportFormat),
                buildSpecification(criteria.getIncludeMetadata(), ExportJob_.includeMetadata),
                buildSpecification(criteria.getIncludeVersions(), ExportJob_.includeVersions),
                buildSpecification(criteria.getIncludeComments(), ExportJob_.includeComments),
                buildSpecification(criteria.getIncludeAuditTrail(), ExportJob_.includeAuditTrail),
                buildStringSpecification(criteria.gets3ExportKey(), ExportJob_.s3ExportKey),
                buildRangeSpecification(criteria.getExportSize(), ExportJob_.exportSize),
                buildRangeSpecification(criteria.getDocumentCount(), ExportJob_.documentCount),
                buildRangeSpecification(criteria.getFilesGenerated(), ExportJob_.filesGenerated),
                buildSpecification(criteria.getStatus(), ExportJob_.status),
                buildRangeSpecification(criteria.getStartDate(), ExportJob_.startDate),
                buildRangeSpecification(criteria.getEndDate(), ExportJob_.endDate),
                buildStringSpecification(criteria.getCreatedBy(), ExportJob_.createdBy),
                buildRangeSpecification(criteria.getCreatedDate(), ExportJob_.createdDate),
                buildSpecification(criteria.getExportResultsId(), root ->
                    root.join(ExportJob_.exportResults, JoinType.LEFT).get(ExportResult_.id)
                ),
                buildSpecification(criteria.getExportPatternId(), root ->
                    root.join(ExportJob_.exportPattern, JoinType.LEFT).get(ExportPattern_.id)
                )
            );
        }
        return specification;
    }
}
