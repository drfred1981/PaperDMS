package fr.smartprod.paperdms.transform.service;

import fr.smartprod.paperdms.transform.domain.*; // for static metamodels
import fr.smartprod.paperdms.transform.domain.TransformConversionJob;
import fr.smartprod.paperdms.transform.repository.TransformConversionJobRepository;
import fr.smartprod.paperdms.transform.service.criteria.TransformConversionJobCriteria;
import fr.smartprod.paperdms.transform.service.dto.TransformConversionJobDTO;
import fr.smartprod.paperdms.transform.service.mapper.TransformConversionJobMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link TransformConversionJob} entities in the database.
 * The main input is a {@link TransformConversionJobCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link TransformConversionJobDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TransformConversionJobQueryService extends QueryService<TransformConversionJob> {

    private static final Logger LOG = LoggerFactory.getLogger(TransformConversionJobQueryService.class);

    private final TransformConversionJobRepository transformConversionJobRepository;

    private final TransformConversionJobMapper transformConversionJobMapper;

    public TransformConversionJobQueryService(
        TransformConversionJobRepository transformConversionJobRepository,
        TransformConversionJobMapper transformConversionJobMapper
    ) {
        this.transformConversionJobRepository = transformConversionJobRepository;
        this.transformConversionJobMapper = transformConversionJobMapper;
    }

    /**
     * Return a {@link Page} of {@link TransformConversionJobDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TransformConversionJobDTO> findByCriteria(TransformConversionJobCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TransformConversionJob> specification = createSpecification(criteria);
        return transformConversionJobRepository.findAll(specification, page).map(transformConversionJobMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TransformConversionJobCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<TransformConversionJob> specification = createSpecification(criteria);
        return transformConversionJobRepository.count(specification);
    }

    /**
     * Function to convert {@link TransformConversionJobCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TransformConversionJob> createSpecification(TransformConversionJobCriteria criteria) {
        Specification<TransformConversionJob> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), TransformConversionJob_.id),
                buildStringSpecification(criteria.getDocumentSha256(), TransformConversionJob_.documentSha256),
                buildStringSpecification(criteria.getSourceFormat(), TransformConversionJob_.sourceFormat),
                buildStringSpecification(criteria.getTargetFormat(), TransformConversionJob_.targetFormat),
                buildStringSpecification(criteria.getConversionEngine(), TransformConversionJob_.conversionEngine),
                buildStringSpecification(criteria.getOutputS3Key(), TransformConversionJob_.outputS3Key),
                buildStringSpecification(criteria.getOutputDocumentSha256(), TransformConversionJob_.outputDocumentSha256),
                buildSpecification(criteria.getStatus(), TransformConversionJob_.status),
                buildRangeSpecification(criteria.getStartDate(), TransformConversionJob_.startDate),
                buildRangeSpecification(criteria.getEndDate(), TransformConversionJob_.endDate),
                buildStringSpecification(criteria.getCreatedBy(), TransformConversionJob_.createdBy),
                buildRangeSpecification(criteria.getCreatedDate(), TransformConversionJob_.createdDate)
            );
        }
        return specification;
    }
}
