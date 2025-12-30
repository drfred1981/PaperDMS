package fr.smartprod.paperdms.export.service;

import fr.smartprod.paperdms.export.domain.*; // for static metamodels
import fr.smartprod.paperdms.export.domain.ExportResult;
import fr.smartprod.paperdms.export.repository.ExportResultRepository;
import fr.smartprod.paperdms.export.service.criteria.ExportResultCriteria;
import fr.smartprod.paperdms.export.service.dto.ExportResultDTO;
import fr.smartprod.paperdms.export.service.mapper.ExportResultMapper;
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
 * Service for executing complex queries for {@link ExportResult} entities in the database.
 * The main input is a {@link ExportResultCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ExportResultDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ExportResultQueryService extends QueryService<ExportResult> {

    private static final Logger LOG = LoggerFactory.getLogger(ExportResultQueryService.class);

    private final ExportResultRepository exportResultRepository;

    private final ExportResultMapper exportResultMapper;

    public ExportResultQueryService(ExportResultRepository exportResultRepository, ExportResultMapper exportResultMapper) {
        this.exportResultRepository = exportResultRepository;
        this.exportResultMapper = exportResultMapper;
    }

    /**
     * Return a {@link Page} of {@link ExportResultDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ExportResultDTO> findByCriteria(ExportResultCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ExportResult> specification = createSpecification(criteria);
        return exportResultRepository.findAll(specification, page).map(exportResultMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ExportResultCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ExportResult> specification = createSpecification(criteria);
        return exportResultRepository.count(specification);
    }

    /**
     * Function to convert {@link ExportResultCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ExportResult> createSpecification(ExportResultCriteria criteria) {
        Specification<ExportResult> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), ExportResult_.id),
                buildStringSpecification(criteria.getDocumentSha256(), ExportResult_.documentSha256),
                buildStringSpecification(criteria.getOriginalFileName(), ExportResult_.originalFileName),
                buildStringSpecification(criteria.getExportedPath(), ExportResult_.exportedPath),
                buildStringSpecification(criteria.getExportedFileName(), ExportResult_.exportedFileName),
                buildStringSpecification(criteria.gets3ExportKey(), ExportResult_.s3ExportKey),
                buildRangeSpecification(criteria.getFileSize(), ExportResult_.fileSize),
                buildSpecification(criteria.getStatus(), ExportResult_.status),
                buildRangeSpecification(criteria.getExportedDate(), ExportResult_.exportedDate),
                buildSpecification(criteria.getExportJobId(), root -> root.join(ExportResult_.exportJob, JoinType.LEFT).get(ExportJob_.id))
            );
        }
        return specification;
    }
}
