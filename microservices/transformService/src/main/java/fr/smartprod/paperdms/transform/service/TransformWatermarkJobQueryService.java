package fr.smartprod.paperdms.transform.service;

import fr.smartprod.paperdms.transform.domain.*; // for static metamodels
import fr.smartprod.paperdms.transform.domain.TransformWatermarkJob;
import fr.smartprod.paperdms.transform.repository.TransformWatermarkJobRepository;
import fr.smartprod.paperdms.transform.service.criteria.TransformWatermarkJobCriteria;
import fr.smartprod.paperdms.transform.service.dto.TransformWatermarkJobDTO;
import fr.smartprod.paperdms.transform.service.mapper.TransformWatermarkJobMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link TransformWatermarkJob} entities in the database.
 * The main input is a {@link TransformWatermarkJobCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link TransformWatermarkJobDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TransformWatermarkJobQueryService extends QueryService<TransformWatermarkJob> {

    private static final Logger LOG = LoggerFactory.getLogger(TransformWatermarkJobQueryService.class);

    private final TransformWatermarkJobRepository transformWatermarkJobRepository;

    private final TransformWatermarkJobMapper transformWatermarkJobMapper;

    public TransformWatermarkJobQueryService(
        TransformWatermarkJobRepository transformWatermarkJobRepository,
        TransformWatermarkJobMapper transformWatermarkJobMapper
    ) {
        this.transformWatermarkJobRepository = transformWatermarkJobRepository;
        this.transformWatermarkJobMapper = transformWatermarkJobMapper;
    }

    /**
     * Return a {@link Page} of {@link TransformWatermarkJobDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TransformWatermarkJobDTO> findByCriteria(TransformWatermarkJobCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TransformWatermarkJob> specification = createSpecification(criteria);
        return transformWatermarkJobRepository.findAll(specification, page).map(transformWatermarkJobMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TransformWatermarkJobCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<TransformWatermarkJob> specification = createSpecification(criteria);
        return transformWatermarkJobRepository.count(specification);
    }

    /**
     * Function to convert {@link TransformWatermarkJobCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TransformWatermarkJob> createSpecification(TransformWatermarkJobCriteria criteria) {
        Specification<TransformWatermarkJob> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), TransformWatermarkJob_.id),
                buildStringSpecification(criteria.getDocumentSha256(), TransformWatermarkJob_.documentSha256),
                buildSpecification(criteria.getWatermarkType(), TransformWatermarkJob_.watermarkType),
                buildStringSpecification(criteria.getWatermarkText(), TransformWatermarkJob_.watermarkText),
                buildStringSpecification(criteria.getWatermarkImageS3Key(), TransformWatermarkJob_.watermarkImageS3Key),
                buildSpecification(criteria.getPosition(), TransformWatermarkJob_.position),
                buildRangeSpecification(criteria.getOpacity(), TransformWatermarkJob_.opacity),
                buildRangeSpecification(criteria.getFontSize(), TransformWatermarkJob_.fontSize),
                buildStringSpecification(criteria.getColor(), TransformWatermarkJob_.color),
                buildRangeSpecification(criteria.getRotation(), TransformWatermarkJob_.rotation),
                buildSpecification(criteria.getTiled(), TransformWatermarkJob_.tiled),
                buildStringSpecification(criteria.getOutputS3Key(), TransformWatermarkJob_.outputS3Key),
                buildStringSpecification(criteria.getOutputDocumentSha256(), TransformWatermarkJob_.outputDocumentSha256),
                buildSpecification(criteria.getStatus(), TransformWatermarkJob_.status),
                buildRangeSpecification(criteria.getStartDate(), TransformWatermarkJob_.startDate),
                buildRangeSpecification(criteria.getEndDate(), TransformWatermarkJob_.endDate),
                buildStringSpecification(criteria.getCreatedBy(), TransformWatermarkJob_.createdBy),
                buildRangeSpecification(criteria.getCreatedDate(), TransformWatermarkJob_.createdDate)
            );
        }
        return specification;
    }
}
