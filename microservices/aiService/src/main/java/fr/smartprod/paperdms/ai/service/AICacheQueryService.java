package fr.smartprod.paperdms.ai.service;

import fr.smartprod.paperdms.ai.domain.*; // for static metamodels
import fr.smartprod.paperdms.ai.domain.AICache;
import fr.smartprod.paperdms.ai.repository.AICacheRepository;
import fr.smartprod.paperdms.ai.repository.search.AICacheSearchRepository;
import fr.smartprod.paperdms.ai.service.criteria.AICacheCriteria;
import fr.smartprod.paperdms.ai.service.dto.AICacheDTO;
import fr.smartprod.paperdms.ai.service.mapper.AICacheMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link AICache} entities in the database.
 * The main input is a {@link AICacheCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AICacheDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AICacheQueryService extends QueryService<AICache> {

    private static final Logger LOG = LoggerFactory.getLogger(AICacheQueryService.class);

    private final AICacheRepository aICacheRepository;

    private final AICacheMapper aICacheMapper;

    private final AICacheSearchRepository aICacheSearchRepository;

    public AICacheQueryService(
        AICacheRepository aICacheRepository,
        AICacheMapper aICacheMapper,
        AICacheSearchRepository aICacheSearchRepository
    ) {
        this.aICacheRepository = aICacheRepository;
        this.aICacheMapper = aICacheMapper;
        this.aICacheSearchRepository = aICacheSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link AICacheDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AICacheDTO> findByCriteria(AICacheCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AICache> specification = createSpecification(criteria);
        return aICacheRepository.findAll(specification, page).map(aICacheMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AICacheCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AICache> specification = createSpecification(criteria);
        return aICacheRepository.count(specification);
    }

    /**
     * Function to convert {@link AICacheCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AICache> createSpecification(AICacheCriteria criteria) {
        Specification<AICache> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), AICache_.id),
                buildStringSpecification(criteria.getCacheKey(), AICache_.cacheKey),
                buildStringSpecification(criteria.getInputSha256(), AICache_.inputSha256),
                buildStringSpecification(criteria.getAiProvider(), AICache_.aiProvider),
                buildStringSpecification(criteria.getAiModel(), AICache_.aiModel),
                buildStringSpecification(criteria.getOperation(), AICache_.operation),
                buildStringSpecification(criteria.gets3ResultKey(), AICache_.s3ResultKey),
                buildRangeSpecification(criteria.getConfidence(), AICache_.confidence),
                buildRangeSpecification(criteria.getHits(), AICache_.hits),
                buildRangeSpecification(criteria.getCost(), AICache_.cost),
                buildRangeSpecification(criteria.getLastAccessDate(), AICache_.lastAccessDate),
                buildRangeSpecification(criteria.getCreatedDate(), AICache_.createdDate),
                buildRangeSpecification(criteria.getExpirationDate(), AICache_.expirationDate)
            );
        }
        return specification;
    }
}
