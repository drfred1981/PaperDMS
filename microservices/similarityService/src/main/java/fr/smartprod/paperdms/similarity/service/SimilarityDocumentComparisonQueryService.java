package fr.smartprod.paperdms.similarity.service;

import fr.smartprod.paperdms.similarity.domain.*; // for static metamodels
import fr.smartprod.paperdms.similarity.domain.SimilarityDocumentComparison;
import fr.smartprod.paperdms.similarity.repository.SimilarityDocumentComparisonRepository;
import fr.smartprod.paperdms.similarity.repository.search.SimilarityDocumentComparisonSearchRepository;
import fr.smartprod.paperdms.similarity.service.criteria.SimilarityDocumentComparisonCriteria;
import fr.smartprod.paperdms.similarity.service.dto.SimilarityDocumentComparisonDTO;
import fr.smartprod.paperdms.similarity.service.mapper.SimilarityDocumentComparisonMapper;
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
 * Service for executing complex queries for {@link SimilarityDocumentComparison} entities in the database.
 * The main input is a {@link SimilarityDocumentComparisonCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link SimilarityDocumentComparisonDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SimilarityDocumentComparisonQueryService extends QueryService<SimilarityDocumentComparison> {

    private static final Logger LOG = LoggerFactory.getLogger(SimilarityDocumentComparisonQueryService.class);

    private final SimilarityDocumentComparisonRepository similarityDocumentComparisonRepository;

    private final SimilarityDocumentComparisonMapper similarityDocumentComparisonMapper;

    private final SimilarityDocumentComparisonSearchRepository similarityDocumentComparisonSearchRepository;

    public SimilarityDocumentComparisonQueryService(
        SimilarityDocumentComparisonRepository similarityDocumentComparisonRepository,
        SimilarityDocumentComparisonMapper similarityDocumentComparisonMapper,
        SimilarityDocumentComparisonSearchRepository similarityDocumentComparisonSearchRepository
    ) {
        this.similarityDocumentComparisonRepository = similarityDocumentComparisonRepository;
        this.similarityDocumentComparisonMapper = similarityDocumentComparisonMapper;
        this.similarityDocumentComparisonSearchRepository = similarityDocumentComparisonSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link SimilarityDocumentComparisonDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SimilarityDocumentComparisonDTO> findByCriteria(SimilarityDocumentComparisonCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SimilarityDocumentComparison> specification = createSpecification(criteria);
        return similarityDocumentComparisonRepository.findAll(specification, page).map(similarityDocumentComparisonMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SimilarityDocumentComparisonCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<SimilarityDocumentComparison> specification = createSpecification(criteria);
        return similarityDocumentComparisonRepository.count(specification);
    }

    /**
     * Function to convert {@link SimilarityDocumentComparisonCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SimilarityDocumentComparison> createSpecification(SimilarityDocumentComparisonCriteria criteria) {
        Specification<SimilarityDocumentComparison> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), SimilarityDocumentComparison_.id),
                buildStringSpecification(criteria.getSourceDocumentSha256(), SimilarityDocumentComparison_.sourceDocumentSha256),
                buildStringSpecification(criteria.getTargetDocumentSha256(), SimilarityDocumentComparison_.targetDocumentSha256),
                buildRangeSpecification(criteria.getSimilarityScore(), SimilarityDocumentComparison_.similarityScore),
                buildSpecification(criteria.getAlgorithm(), SimilarityDocumentComparison_.algorithm),
                buildRangeSpecification(criteria.getComputedDate(), SimilarityDocumentComparison_.computedDate),
                buildSpecification(criteria.getIsRelevant(), SimilarityDocumentComparison_.isRelevant),
                buildStringSpecification(criteria.getReviewedBy(), SimilarityDocumentComparison_.reviewedBy),
                buildRangeSpecification(criteria.getReviewedDate(), SimilarityDocumentComparison_.reviewedDate),
                buildSpecification(criteria.getJobId(), root ->
                    root.join(SimilarityDocumentComparison_.job, JoinType.LEFT).get(SimilarityJob_.id)
                )
            );
        }
        return specification;
    }
}
