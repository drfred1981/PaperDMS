package fr.smartprod.paperdms.transform.service;

import fr.smartprod.paperdms.transform.domain.*; // for static metamodels
import fr.smartprod.paperdms.transform.domain.TransformMergeJob;
import fr.smartprod.paperdms.transform.repository.TransformMergeJobRepository;
import fr.smartprod.paperdms.transform.service.criteria.TransformMergeJobCriteria;
import fr.smartprod.paperdms.transform.service.dto.TransformMergeJobDTO;
import fr.smartprod.paperdms.transform.service.mapper.TransformMergeJobMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link TransformMergeJob} entities in the database.
 * The main input is a {@link TransformMergeJobCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link TransformMergeJobDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TransformMergeJobQueryService extends QueryService<TransformMergeJob> {

    private static final Logger LOG = LoggerFactory.getLogger(TransformMergeJobQueryService.class);

    private final TransformMergeJobRepository transformMergeJobRepository;

    private final TransformMergeJobMapper transformMergeJobMapper;

    public TransformMergeJobQueryService(
        TransformMergeJobRepository transformMergeJobRepository,
        TransformMergeJobMapper transformMergeJobMapper
    ) {
        this.transformMergeJobRepository = transformMergeJobRepository;
        this.transformMergeJobMapper = transformMergeJobMapper;
    }

    /**
     * Return a {@link Page} of {@link TransformMergeJobDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TransformMergeJobDTO> findByCriteria(TransformMergeJobCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TransformMergeJob> specification = createSpecification(criteria);
        return transformMergeJobRepository.findAll(specification, page).map(transformMergeJobMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TransformMergeJobCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<TransformMergeJob> specification = createSpecification(criteria);
        return transformMergeJobRepository.count(specification);
    }

    /**
     * Function to convert {@link TransformMergeJobCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TransformMergeJob> createSpecification(TransformMergeJobCriteria criteria) {
        Specification<TransformMergeJob> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), TransformMergeJob_.id),
                buildStringSpecification(criteria.getName(), TransformMergeJob_.name),
                buildSpecification(criteria.getIncludeBookmarks(), TransformMergeJob_.includeBookmarks),
                buildSpecification(criteria.getIncludeToc(), TransformMergeJob_.includeToc),
                buildSpecification(criteria.getAddPageNumbers(), TransformMergeJob_.addPageNumbers),
                buildStringSpecification(criteria.getOutputS3Key(), TransformMergeJob_.outputS3Key),
                buildStringSpecification(criteria.getOutputDocumentSha256(), TransformMergeJob_.outputDocumentSha256),
                buildSpecification(criteria.getStatus(), TransformMergeJob_.status),
                buildRangeSpecification(criteria.getStartDate(), TransformMergeJob_.startDate),
                buildRangeSpecification(criteria.getEndDate(), TransformMergeJob_.endDate),
                buildStringSpecification(criteria.getCreatedBy(), TransformMergeJob_.createdBy),
                buildRangeSpecification(criteria.getCreatedDate(), TransformMergeJob_.createdDate)
            );
        }
        return specification;
    }
}
