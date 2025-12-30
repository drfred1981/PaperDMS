package fr.smartprod.paperdms.similarity.service;

import fr.smartprod.paperdms.similarity.domain.*; // for static metamodels
import fr.smartprod.paperdms.similarity.domain.SimilarityJob;
import fr.smartprod.paperdms.similarity.repository.SimilarityJobRepository;
import fr.smartprod.paperdms.similarity.repository.search.SimilarityJobSearchRepository;
import fr.smartprod.paperdms.similarity.service.criteria.SimilarityJobCriteria;
import fr.smartprod.paperdms.similarity.service.dto.SimilarityJobDTO;
import fr.smartprod.paperdms.similarity.service.mapper.SimilarityJobMapper;
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
 * Service for executing complex queries for {@link SimilarityJob} entities in the database.
 * The main input is a {@link SimilarityJobCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link SimilarityJobDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SimilarityJobQueryService extends QueryService<SimilarityJob> {

    private static final Logger LOG = LoggerFactory.getLogger(SimilarityJobQueryService.class);

    private final SimilarityJobRepository similarityJobRepository;

    private final SimilarityJobMapper similarityJobMapper;

    private final SimilarityJobSearchRepository similarityJobSearchRepository;

    public SimilarityJobQueryService(
        SimilarityJobRepository similarityJobRepository,
        SimilarityJobMapper similarityJobMapper,
        SimilarityJobSearchRepository similarityJobSearchRepository
    ) {
        this.similarityJobRepository = similarityJobRepository;
        this.similarityJobMapper = similarityJobMapper;
        this.similarityJobSearchRepository = similarityJobSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link SimilarityJobDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SimilarityJobDTO> findByCriteria(SimilarityJobCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SimilarityJob> specification = createSpecification(criteria);
        return similarityJobRepository.findAll(specification, page).map(similarityJobMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SimilarityJobCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<SimilarityJob> specification = createSpecification(criteria);
        return similarityJobRepository.count(specification);
    }

    /**
     * Function to convert {@link SimilarityJobCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SimilarityJob> createSpecification(SimilarityJobCriteria criteria) {
        Specification<SimilarityJob> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), SimilarityJob_.id),
                buildStringSpecification(criteria.getDocumentSha256(), SimilarityJob_.documentSha256),
                buildSpecification(criteria.getStatus(), SimilarityJob_.status),
                buildSpecification(criteria.getAlgorithm(), SimilarityJob_.algorithm),
                buildSpecification(criteria.getScope(), SimilarityJob_.scope),
                buildRangeSpecification(criteria.getMinSimilarityThreshold(), SimilarityJob_.minSimilarityThreshold),
                buildRangeSpecification(criteria.getMatchesFound(), SimilarityJob_.matchesFound),
                buildRangeSpecification(criteria.getStartDate(), SimilarityJob_.startDate),
                buildRangeSpecification(criteria.getEndDate(), SimilarityJob_.endDate),
                buildRangeSpecification(criteria.getCreatedDate(), SimilarityJob_.createdDate),
                buildStringSpecification(criteria.getCreatedBy(), SimilarityJob_.createdBy),
                buildSpecification(criteria.getSimilaritiesId(), root ->
                    root.join(SimilarityJob_.similarities, JoinType.LEFT).get(SimilarityDocumentComparison_.id)
                )
            );
        }
        return specification;
    }
}
