package fr.smartprod.paperdms.search.service;

import fr.smartprod.paperdms.search.domain.*; // for static metamodels
import fr.smartprod.paperdms.search.domain.SearchFacet;
import fr.smartprod.paperdms.search.repository.SearchFacetRepository;
import fr.smartprod.paperdms.search.repository.search.SearchFacetSearchRepository;
import fr.smartprod.paperdms.search.service.criteria.SearchFacetCriteria;
import fr.smartprod.paperdms.search.service.dto.SearchFacetDTO;
import fr.smartprod.paperdms.search.service.mapper.SearchFacetMapper;
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
 * Service for executing complex queries for {@link SearchFacet} entities in the database.
 * The main input is a {@link SearchFacetCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link SearchFacetDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SearchFacetQueryService extends QueryService<SearchFacet> {

    private static final Logger LOG = LoggerFactory.getLogger(SearchFacetQueryService.class);

    private final SearchFacetRepository searchFacetRepository;

    private final SearchFacetMapper searchFacetMapper;

    private final SearchFacetSearchRepository searchFacetSearchRepository;

    public SearchFacetQueryService(
        SearchFacetRepository searchFacetRepository,
        SearchFacetMapper searchFacetMapper,
        SearchFacetSearchRepository searchFacetSearchRepository
    ) {
        this.searchFacetRepository = searchFacetRepository;
        this.searchFacetMapper = searchFacetMapper;
        this.searchFacetSearchRepository = searchFacetSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link SearchFacetDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SearchFacetDTO> findByCriteria(SearchFacetCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SearchFacet> specification = createSpecification(criteria);
        return searchFacetRepository.findAll(specification, page).map(searchFacetMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SearchFacetCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<SearchFacet> specification = createSpecification(criteria);
        return searchFacetRepository.count(specification);
    }

    /**
     * Function to convert {@link SearchFacetCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SearchFacet> createSpecification(SearchFacetCriteria criteria) {
        Specification<SearchFacet> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), SearchFacet_.id),
                buildStringSpecification(criteria.getFacetName(), SearchFacet_.facetName),
                buildSpecification(criteria.getFacetType(), SearchFacet_.facetType),
                buildSpecification(criteria.getSearchQueryId(), root ->
                    root.join(SearchFacet_.searchQuery, JoinType.LEFT).get(SearchQuery_.id)
                )
            );
        }
        return specification;
    }
}
