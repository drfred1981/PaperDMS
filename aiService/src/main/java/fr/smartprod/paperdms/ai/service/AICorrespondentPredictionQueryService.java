package fr.smartprod.paperdms.ai.service;

import fr.smartprod.paperdms.ai.domain.*; // for static metamodels
import fr.smartprod.paperdms.ai.domain.AICorrespondentPrediction;
import fr.smartprod.paperdms.ai.repository.AICorrespondentPredictionRepository;
import fr.smartprod.paperdms.ai.repository.search.AICorrespondentPredictionSearchRepository;
import fr.smartprod.paperdms.ai.service.criteria.AICorrespondentPredictionCriteria;
import fr.smartprod.paperdms.ai.service.dto.AICorrespondentPredictionDTO;
import fr.smartprod.paperdms.ai.service.mapper.AICorrespondentPredictionMapper;
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
 * Service for executing complex queries for {@link AICorrespondentPrediction} entities in the database.
 * The main input is a {@link AICorrespondentPredictionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AICorrespondentPredictionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AICorrespondentPredictionQueryService extends QueryService<AICorrespondentPrediction> {

    private static final Logger LOG = LoggerFactory.getLogger(AICorrespondentPredictionQueryService.class);

    private final AICorrespondentPredictionRepository aICorrespondentPredictionRepository;

    private final AICorrespondentPredictionMapper aICorrespondentPredictionMapper;

    private final AICorrespondentPredictionSearchRepository aICorrespondentPredictionSearchRepository;

    public AICorrespondentPredictionQueryService(
        AICorrespondentPredictionRepository aICorrespondentPredictionRepository,
        AICorrespondentPredictionMapper aICorrespondentPredictionMapper,
        AICorrespondentPredictionSearchRepository aICorrespondentPredictionSearchRepository
    ) {
        this.aICorrespondentPredictionRepository = aICorrespondentPredictionRepository;
        this.aICorrespondentPredictionMapper = aICorrespondentPredictionMapper;
        this.aICorrespondentPredictionSearchRepository = aICorrespondentPredictionSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link AICorrespondentPredictionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AICorrespondentPredictionDTO> findByCriteria(AICorrespondentPredictionCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AICorrespondentPrediction> specification = createSpecification(criteria);
        return aICorrespondentPredictionRepository.findAll(specification, page).map(aICorrespondentPredictionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AICorrespondentPredictionCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AICorrespondentPrediction> specification = createSpecification(criteria);
        return aICorrespondentPredictionRepository.count(specification);
    }

    /**
     * Function to convert {@link AICorrespondentPredictionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AICorrespondentPrediction> createSpecification(AICorrespondentPredictionCriteria criteria) {
        Specification<AICorrespondentPrediction> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), AICorrespondentPrediction_.id),
                buildStringSpecification(criteria.getCorrespondentName(), AICorrespondentPrediction_.correspondentName),
                buildStringSpecification(criteria.getName(), AICorrespondentPrediction_.name),
                buildStringSpecification(criteria.getEmail(), AICorrespondentPrediction_.email),
                buildStringSpecification(criteria.getPhone(), AICorrespondentPrediction_.phone),
                buildStringSpecification(criteria.getCompany(), AICorrespondentPrediction_.company),
                buildSpecification(criteria.getType(), AICorrespondentPrediction_.type),
                buildSpecification(criteria.getRole(), AICorrespondentPrediction_.role),
                buildRangeSpecification(criteria.getConfidence(), AICorrespondentPrediction_.confidence),
                buildStringSpecification(criteria.getReason(), AICorrespondentPrediction_.reason),
                buildStringSpecification(criteria.getModelVersion(), AICorrespondentPrediction_.modelVersion),
                buildStringSpecification(criteria.getPredictionS3Key(), AICorrespondentPrediction_.predictionS3Key),
                buildSpecification(criteria.getIsAccepted(), AICorrespondentPrediction_.isAccepted),
                buildStringSpecification(criteria.getAcceptedBy(), AICorrespondentPrediction_.acceptedBy),
                buildRangeSpecification(criteria.getAcceptedDate(), AICorrespondentPrediction_.acceptedDate),
                buildRangeSpecification(criteria.getPredictionDate(), AICorrespondentPrediction_.predictionDate),
                buildSpecification(criteria.getJobId(), root ->
                    root.join(AICorrespondentPrediction_.job, JoinType.LEFT).get(AIAutoTagJob_.id)
                )
            );
        }
        return specification;
    }
}
