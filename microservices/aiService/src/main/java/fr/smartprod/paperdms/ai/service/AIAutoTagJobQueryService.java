package fr.smartprod.paperdms.ai.service;

import fr.smartprod.paperdms.ai.domain.*; // for static metamodels
import fr.smartprod.paperdms.ai.domain.AIAutoTagJob;
import fr.smartprod.paperdms.ai.repository.AIAutoTagJobRepository;
import fr.smartprod.paperdms.ai.repository.search.AIAutoTagJobSearchRepository;
import fr.smartprod.paperdms.ai.service.criteria.AIAutoTagJobCriteria;
import fr.smartprod.paperdms.ai.service.dto.AIAutoTagJobDTO;
import fr.smartprod.paperdms.ai.service.mapper.AIAutoTagJobMapper;
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
 * Service for executing complex queries for {@link AIAutoTagJob} entities in the database.
 * The main input is a {@link AIAutoTagJobCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AIAutoTagJobDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AIAutoTagJobQueryService extends QueryService<AIAutoTagJob> {

    private static final Logger LOG = LoggerFactory.getLogger(AIAutoTagJobQueryService.class);

    private final AIAutoTagJobRepository aIAutoTagJobRepository;

    private final AIAutoTagJobMapper aIAutoTagJobMapper;

    private final AIAutoTagJobSearchRepository aIAutoTagJobSearchRepository;

    public AIAutoTagJobQueryService(
        AIAutoTagJobRepository aIAutoTagJobRepository,
        AIAutoTagJobMapper aIAutoTagJobMapper,
        AIAutoTagJobSearchRepository aIAutoTagJobSearchRepository
    ) {
        this.aIAutoTagJobRepository = aIAutoTagJobRepository;
        this.aIAutoTagJobMapper = aIAutoTagJobMapper;
        this.aIAutoTagJobSearchRepository = aIAutoTagJobSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link AIAutoTagJobDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AIAutoTagJobDTO> findByCriteria(AIAutoTagJobCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AIAutoTagJob> specification = createSpecification(criteria);
        return aIAutoTagJobRepository.findAll(specification, page).map(aIAutoTagJobMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AIAutoTagJobCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AIAutoTagJob> specification = createSpecification(criteria);
        return aIAutoTagJobRepository.count(specification);
    }

    /**
     * Function to convert {@link AIAutoTagJobCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AIAutoTagJob> createSpecification(AIAutoTagJobCriteria criteria) {
        Specification<AIAutoTagJob> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), AIAutoTagJob_.id),
                buildStringSpecification(criteria.getDocumentSha256(), AIAutoTagJob_.documentSha256),
                buildStringSpecification(criteria.gets3Key(), AIAutoTagJob_.s3Key),
                buildStringSpecification(criteria.getExtractedTextSha256(), AIAutoTagJob_.extractedTextSha256),
                buildSpecification(criteria.getStatus(), AIAutoTagJob_.status),
                buildStringSpecification(criteria.getModelVersion(), AIAutoTagJob_.modelVersion),
                buildStringSpecification(criteria.getResultCacheKey(), AIAutoTagJob_.resultCacheKey),
                buildSpecification(criteria.getIsCached(), AIAutoTagJob_.isCached),
                buildRangeSpecification(criteria.getStartDate(), AIAutoTagJob_.startDate),
                buildRangeSpecification(criteria.getEndDate(), AIAutoTagJob_.endDate),
                buildRangeSpecification(criteria.getCreatedDate(), AIAutoTagJob_.createdDate),
                buildSpecification(criteria.getAITypePredictionId(), root ->
                    root.join(AIAutoTagJob_.aITypePrediction, JoinType.LEFT).get(AITypePrediction_.id)
                ),
                buildSpecification(criteria.getLanguagePredictionId(), root ->
                    root.join(AIAutoTagJob_.languagePrediction, JoinType.LEFT).get(AILanguageDetection_.id)
                ),
                buildSpecification(criteria.getAITagPredictionsId(), root ->
                    root.join(AIAutoTagJob_.aITagPredictions, JoinType.LEFT).get(AITagPrediction_.id)
                ),
                buildSpecification(criteria.getAICorrespondentPredictionsId(), root ->
                    root.join(AIAutoTagJob_.aICorrespondentPredictions, JoinType.LEFT).get(AICorrespondentPrediction_.id)
                )
            );
        }
        return specification;
    }
}
