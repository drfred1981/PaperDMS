package fr.smartprod.paperdms.ai.service;

import fr.smartprod.paperdms.ai.domain.*; // for static metamodels
import fr.smartprod.paperdms.ai.domain.AITagPrediction;
import fr.smartprod.paperdms.ai.repository.AITagPredictionRepository;
import fr.smartprod.paperdms.ai.repository.search.AITagPredictionSearchRepository;
import fr.smartprod.paperdms.ai.service.criteria.AITagPredictionCriteria;
import fr.smartprod.paperdms.ai.service.dto.AITagPredictionDTO;
import fr.smartprod.paperdms.ai.service.mapper.AITagPredictionMapper;
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
 * Service for executing complex queries for {@link AITagPrediction} entities in the database.
 * The main input is a {@link AITagPredictionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AITagPredictionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AITagPredictionQueryService extends QueryService<AITagPrediction> {

    private static final Logger LOG = LoggerFactory.getLogger(AITagPredictionQueryService.class);

    private final AITagPredictionRepository aITagPredictionRepository;

    private final AITagPredictionMapper aITagPredictionMapper;

    private final AITagPredictionSearchRepository aITagPredictionSearchRepository;

    public AITagPredictionQueryService(
        AITagPredictionRepository aITagPredictionRepository,
        AITagPredictionMapper aITagPredictionMapper,
        AITagPredictionSearchRepository aITagPredictionSearchRepository
    ) {
        this.aITagPredictionRepository = aITagPredictionRepository;
        this.aITagPredictionMapper = aITagPredictionMapper;
        this.aITagPredictionSearchRepository = aITagPredictionSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link AITagPredictionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AITagPredictionDTO> findByCriteria(AITagPredictionCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AITagPrediction> specification = createSpecification(criteria);
        return aITagPredictionRepository.findAll(specification, page).map(aITagPredictionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AITagPredictionCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AITagPrediction> specification = createSpecification(criteria);
        return aITagPredictionRepository.count(specification);
    }

    /**
     * Function to convert {@link AITagPredictionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AITagPrediction> createSpecification(AITagPredictionCriteria criteria) {
        Specification<AITagPrediction> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), AITagPrediction_.id),
                buildStringSpecification(criteria.getTagName(), AITagPrediction_.tagName),
                buildRangeSpecification(criteria.getConfidence(), AITagPrediction_.confidence),
                buildStringSpecification(criteria.getReason(), AITagPrediction_.reason),
                buildStringSpecification(criteria.getModelVersion(), AITagPrediction_.modelVersion),
                buildStringSpecification(criteria.getPredictionS3Key(), AITagPrediction_.predictionS3Key),
                buildSpecification(criteria.getIsAccepted(), AITagPrediction_.isAccepted),
                buildStringSpecification(criteria.getAcceptedBy(), AITagPrediction_.acceptedBy),
                buildRangeSpecification(criteria.getAcceptedDate(), AITagPrediction_.acceptedDate),
                buildRangeSpecification(criteria.getPredictionDate(), AITagPrediction_.predictionDate),
                buildSpecification(criteria.getJobId(), root -> root.join(AITagPrediction_.job, JoinType.LEFT).get(AIAutoTagJob_.id))
            );
        }
        return specification;
    }
}
