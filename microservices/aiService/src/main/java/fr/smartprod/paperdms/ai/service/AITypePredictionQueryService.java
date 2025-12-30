package fr.smartprod.paperdms.ai.service;

import fr.smartprod.paperdms.ai.domain.*; // for static metamodels
import fr.smartprod.paperdms.ai.domain.AITypePrediction;
import fr.smartprod.paperdms.ai.repository.AITypePredictionRepository;
import fr.smartprod.paperdms.ai.repository.search.AITypePredictionSearchRepository;
import fr.smartprod.paperdms.ai.service.criteria.AITypePredictionCriteria;
import fr.smartprod.paperdms.ai.service.dto.AITypePredictionDTO;
import fr.smartprod.paperdms.ai.service.mapper.AITypePredictionMapper;
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
 * Service for executing complex queries for {@link AITypePrediction} entities in the database.
 * The main input is a {@link AITypePredictionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AITypePredictionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AITypePredictionQueryService extends QueryService<AITypePrediction> {

    private static final Logger LOG = LoggerFactory.getLogger(AITypePredictionQueryService.class);

    private final AITypePredictionRepository aITypePredictionRepository;

    private final AITypePredictionMapper aITypePredictionMapper;

    private final AITypePredictionSearchRepository aITypePredictionSearchRepository;

    public AITypePredictionQueryService(
        AITypePredictionRepository aITypePredictionRepository,
        AITypePredictionMapper aITypePredictionMapper,
        AITypePredictionSearchRepository aITypePredictionSearchRepository
    ) {
        this.aITypePredictionRepository = aITypePredictionRepository;
        this.aITypePredictionMapper = aITypePredictionMapper;
        this.aITypePredictionSearchRepository = aITypePredictionSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link AITypePredictionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AITypePredictionDTO> findByCriteria(AITypePredictionCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AITypePrediction> specification = createSpecification(criteria);
        return aITypePredictionRepository.findAll(specification, page).map(aITypePredictionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AITypePredictionCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AITypePrediction> specification = createSpecification(criteria);
        return aITypePredictionRepository.count(specification);
    }

    /**
     * Function to convert {@link AITypePredictionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AITypePrediction> createSpecification(AITypePredictionCriteria criteria) {
        Specification<AITypePrediction> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), AITypePrediction_.id),
                buildStringSpecification(criteria.getDocumentTypeName(), AITypePrediction_.documentTypeName),
                buildRangeSpecification(criteria.getConfidence(), AITypePrediction_.confidence),
                buildStringSpecification(criteria.getReason(), AITypePrediction_.reason),
                buildStringSpecification(criteria.getModelVersion(), AITypePrediction_.modelVersion),
                buildStringSpecification(criteria.getPredictionS3Key(), AITypePrediction_.predictionS3Key),
                buildSpecification(criteria.getIsAccepted(), AITypePrediction_.isAccepted),
                buildStringSpecification(criteria.getAcceptedBy(), AITypePrediction_.acceptedBy),
                buildRangeSpecification(criteria.getAcceptedDate(), AITypePrediction_.acceptedDate),
                buildRangeSpecification(criteria.getPredictionDate(), AITypePrediction_.predictionDate),
                buildSpecification(criteria.getJobId(), root -> root.join(AITypePrediction_.job, JoinType.LEFT).get(AIAutoTagJob_.id))
            );
        }
        return specification;
    }
}
