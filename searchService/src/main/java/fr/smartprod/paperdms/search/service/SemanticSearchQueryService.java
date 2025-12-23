package fr.smartprod.paperdms.search.service;

import fr.smartprod.paperdms.search.domain.*; // for static metamodels
import fr.smartprod.paperdms.search.domain.SemanticSearch;
import fr.smartprod.paperdms.search.repository.SemanticSearchRepository;
import fr.smartprod.paperdms.search.service.criteria.SemanticSearchCriteria;
import fr.smartprod.paperdms.search.service.dto.SemanticSearchDTO;
import fr.smartprod.paperdms.search.service.mapper.SemanticSearchMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link SemanticSearch} entities in the database.
 * The main input is a {@link SemanticSearchCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link SemanticSearchDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SemanticSearchQueryService extends QueryService<SemanticSearch> {

    private static final Logger LOG = LoggerFactory.getLogger(SemanticSearchQueryService.class);

    private final SemanticSearchRepository semanticSearchRepository;

    private final SemanticSearchMapper semanticSearchMapper;

    public SemanticSearchQueryService(SemanticSearchRepository semanticSearchRepository, SemanticSearchMapper semanticSearchMapper) {
        this.semanticSearchRepository = semanticSearchRepository;
        this.semanticSearchMapper = semanticSearchMapper;
    }

    /**
     * Return a {@link Page} of {@link SemanticSearchDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SemanticSearchDTO> findByCriteria(SemanticSearchCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SemanticSearch> specification = createSpecification(criteria);
        return semanticSearchRepository.findAll(specification, page).map(semanticSearchMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SemanticSearchCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<SemanticSearch> specification = createSpecification(criteria);
        return semanticSearchRepository.count(specification);
    }

    /**
     * Function to convert {@link SemanticSearchCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SemanticSearch> createSpecification(SemanticSearchCriteria criteria) {
        Specification<SemanticSearch> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), SemanticSearch_.id),
                buildStringSpecification(criteria.getQuery(), SemanticSearch_.query),
                buildStringSpecification(criteria.getModelUsed(), SemanticSearch_.modelUsed),
                buildRangeSpecification(criteria.getExecutionTime(), SemanticSearch_.executionTime),
                buildStringSpecification(criteria.getUserId(), SemanticSearch_.userId),
                buildRangeSpecification(criteria.getSearchDate(), SemanticSearch_.searchDate)
            );
        }
        return specification;
    }
}
