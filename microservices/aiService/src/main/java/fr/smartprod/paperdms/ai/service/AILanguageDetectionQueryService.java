package fr.smartprod.paperdms.ai.service;

import fr.smartprod.paperdms.ai.domain.*; // for static metamodels
import fr.smartprod.paperdms.ai.domain.AILanguageDetection;
import fr.smartprod.paperdms.ai.repository.AILanguageDetectionRepository;
import fr.smartprod.paperdms.ai.repository.search.AILanguageDetectionSearchRepository;
import fr.smartprod.paperdms.ai.service.criteria.AILanguageDetectionCriteria;
import fr.smartprod.paperdms.ai.service.dto.AILanguageDetectionDTO;
import fr.smartprod.paperdms.ai.service.mapper.AILanguageDetectionMapper;
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
 * Service for executing complex queries for {@link AILanguageDetection} entities in the database.
 * The main input is a {@link AILanguageDetectionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AILanguageDetectionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AILanguageDetectionQueryService extends QueryService<AILanguageDetection> {

    private static final Logger LOG = LoggerFactory.getLogger(AILanguageDetectionQueryService.class);

    private final AILanguageDetectionRepository aILanguageDetectionRepository;

    private final AILanguageDetectionMapper aILanguageDetectionMapper;

    private final AILanguageDetectionSearchRepository aILanguageDetectionSearchRepository;

    public AILanguageDetectionQueryService(
        AILanguageDetectionRepository aILanguageDetectionRepository,
        AILanguageDetectionMapper aILanguageDetectionMapper,
        AILanguageDetectionSearchRepository aILanguageDetectionSearchRepository
    ) {
        this.aILanguageDetectionRepository = aILanguageDetectionRepository;
        this.aILanguageDetectionMapper = aILanguageDetectionMapper;
        this.aILanguageDetectionSearchRepository = aILanguageDetectionSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link AILanguageDetectionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AILanguageDetectionDTO> findByCriteria(AILanguageDetectionCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AILanguageDetection> specification = createSpecification(criteria);
        return aILanguageDetectionRepository.findAll(specification, page).map(aILanguageDetectionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AILanguageDetectionCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AILanguageDetection> specification = createSpecification(criteria);
        return aILanguageDetectionRepository.count(specification);
    }

    /**
     * Function to convert {@link AILanguageDetectionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AILanguageDetection> createSpecification(AILanguageDetectionCriteria criteria) {
        Specification<AILanguageDetection> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), AILanguageDetection_.id),
                buildStringSpecification(criteria.getDocumentSha256(), AILanguageDetection_.documentSha256),
                buildStringSpecification(criteria.getDetectedLanguage(), AILanguageDetection_.detectedLanguage),
                buildRangeSpecification(criteria.getConfidence(), AILanguageDetection_.confidence),
                buildSpecification(criteria.getDetectionMethod(), AILanguageDetection_.detectionMethod),
                buildStringSpecification(criteria.getResultCacheKey(), AILanguageDetection_.resultCacheKey),
                buildSpecification(criteria.getIsCached(), AILanguageDetection_.isCached),
                buildRangeSpecification(criteria.getDetectedDate(), AILanguageDetection_.detectedDate),
                buildStringSpecification(criteria.getModelVersion(), AILanguageDetection_.modelVersion),
                buildSpecification(criteria.getJobId(), root -> root.join(AILanguageDetection_.job, JoinType.LEFT).get(AIAutoTagJob_.id))
            );
        }
        return specification;
    }
}
