package fr.smartprod.paperdms.transform.service;

import fr.smartprod.paperdms.transform.domain.*; // for static metamodels
import fr.smartprod.paperdms.transform.domain.TransformCompressionJob;
import fr.smartprod.paperdms.transform.repository.TransformCompressionJobRepository;
import fr.smartprod.paperdms.transform.service.criteria.TransformCompressionJobCriteria;
import fr.smartprod.paperdms.transform.service.dto.TransformCompressionJobDTO;
import fr.smartprod.paperdms.transform.service.mapper.TransformCompressionJobMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link TransformCompressionJob} entities in the database.
 * The main input is a {@link TransformCompressionJobCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link TransformCompressionJobDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TransformCompressionJobQueryService extends QueryService<TransformCompressionJob> {

    private static final Logger LOG = LoggerFactory.getLogger(TransformCompressionJobQueryService.class);

    private final TransformCompressionJobRepository transformCompressionJobRepository;

    private final TransformCompressionJobMapper transformCompressionJobMapper;

    public TransformCompressionJobQueryService(
        TransformCompressionJobRepository transformCompressionJobRepository,
        TransformCompressionJobMapper transformCompressionJobMapper
    ) {
        this.transformCompressionJobRepository = transformCompressionJobRepository;
        this.transformCompressionJobMapper = transformCompressionJobMapper;
    }

    /**
     * Return a {@link Page} of {@link TransformCompressionJobDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TransformCompressionJobDTO> findByCriteria(TransformCompressionJobCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TransformCompressionJob> specification = createSpecification(criteria);
        return transformCompressionJobRepository.findAll(specification, page).map(transformCompressionJobMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TransformCompressionJobCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<TransformCompressionJob> specification = createSpecification(criteria);
        return transformCompressionJobRepository.count(specification);
    }

    /**
     * Function to convert {@link TransformCompressionJobCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TransformCompressionJob> createSpecification(TransformCompressionJobCriteria criteria) {
        Specification<TransformCompressionJob> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), TransformCompressionJob_.id),
                buildStringSpecification(criteria.getDocumentSha256(), TransformCompressionJob_.documentSha256),
                buildSpecification(criteria.getCompressionType(), TransformCompressionJob_.compressionType),
                buildRangeSpecification(criteria.getQuality(), TransformCompressionJob_.quality),
                buildRangeSpecification(criteria.getTargetSizeKb(), TransformCompressionJob_.targetSizeKb),
                buildRangeSpecification(criteria.getOriginalSize(), TransformCompressionJob_.originalSize),
                buildRangeSpecification(criteria.getCompressedSize(), TransformCompressionJob_.compressedSize),
                buildRangeSpecification(criteria.getCompressionRatio(), TransformCompressionJob_.compressionRatio),
                buildStringSpecification(criteria.getOutputS3Key(), TransformCompressionJob_.outputS3Key),
                buildStringSpecification(criteria.getOutputDocumentSha256(), TransformCompressionJob_.outputDocumentSha256),
                buildSpecification(criteria.getStatus(), TransformCompressionJob_.status),
                buildRangeSpecification(criteria.getStartDate(), TransformCompressionJob_.startDate),
                buildRangeSpecification(criteria.getEndDate(), TransformCompressionJob_.endDate),
                buildStringSpecification(criteria.getCreatedBy(), TransformCompressionJob_.createdBy),
                buildRangeSpecification(criteria.getCreatedDate(), TransformCompressionJob_.createdDate)
            );
        }
        return specification;
    }
}
