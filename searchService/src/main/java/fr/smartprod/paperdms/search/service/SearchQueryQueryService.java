package fr.smartprod.paperdms.search.service;

import fr.smartprod.paperdms.search.domain.*; // for static metamodels
import fr.smartprod.paperdms.search.domain.SearchQuery;
import fr.smartprod.paperdms.search.repository.SearchQueryRepository;
import fr.smartprod.paperdms.search.service.criteria.SearchQueryCriteria;
import fr.smartprod.paperdms.search.service.dto.SearchQueryDTO;
import fr.smartprod.paperdms.search.service.mapper.SearchQueryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link SearchQuery} entities in the database.
 * The main input is a {@link SearchQueryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link SearchQueryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SearchQueryQueryService extends QueryService<SearchQuery> {

    private static final Logger LOG = LoggerFactory.getLogger(SearchQueryQueryService.class);

    private final SearchQueryRepository searchQueryRepository;

    private final SearchQueryMapper searchQueryMapper;

    public SearchQueryQueryService(SearchQueryRepository searchQueryRepository, SearchQueryMapper searchQueryMapper) {
        this.searchQueryRepository = searchQueryRepository;
        this.searchQueryMapper = searchQueryMapper;
    }

    /**
     * Return a {@link Page} of {@link SearchQueryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SearchQueryDTO> findByCriteria(SearchQueryCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SearchQuery> specification = createSpecification(criteria);
        return searchQueryRepository.findAll(specification, page).map(searchQueryMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SearchQueryCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<SearchQuery> specification = createSpecification(criteria);
        return searchQueryRepository.count(specification);
    }

    /**
     * Function to convert {@link SearchQueryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SearchQuery> createSpecification(SearchQueryCriteria criteria) {
        Specification<SearchQuery> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), SearchQuery_.id),
                buildStringSpecification(criteria.getQuery(), SearchQuery_.query),
                buildRangeSpecification(criteria.getResultCount(), SearchQuery_.resultCount),
                buildRangeSpecification(criteria.getExecutionTime(), SearchQuery_.executionTime),
                buildStringSpecification(criteria.getUserId(), SearchQuery_.userId),
                buildRangeSpecification(criteria.getSearchDate(), SearchQuery_.searchDate),
                buildSpecification(criteria.getIsRelevant(), SearchQuery_.isRelevant)
            );
        }
        return specification;
    }
}
