package fr.smartprod.paperdms.search.service;

import fr.smartprod.paperdms.search.domain.*; // for static metamodels
import fr.smartprod.paperdms.search.domain.SearchSemantic;
import fr.smartprod.paperdms.search.repository.SearchSemanticRepository;
import fr.smartprod.paperdms.search.repository.search.SearchSemanticSearchRepository;
import fr.smartprod.paperdms.search.service.criteria.SearchSemanticCriteria;
import fr.smartprod.paperdms.search.service.dto.SearchSemanticDTO;
import fr.smartprod.paperdms.search.service.mapper.SearchSemanticMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link SearchSemantic} entities in the database.
 * The main input is a {@link SearchSemanticCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link SearchSemanticDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SearchSemanticQueryService extends QueryService<SearchSemantic> {

    private static final Logger LOG = LoggerFactory.getLogger(SearchSemanticQueryService.class);

    private final SearchSemanticRepository searchSemanticRepository;

    private final SearchSemanticMapper searchSemanticMapper;

    private final SearchSemanticSearchRepository searchSemanticSearchRepository;

    public SearchSemanticQueryService(
        SearchSemanticRepository searchSemanticRepository,
        SearchSemanticMapper searchSemanticMapper,
        SearchSemanticSearchRepository searchSemanticSearchRepository
    ) {
        this.searchSemanticRepository = searchSemanticRepository;
        this.searchSemanticMapper = searchSemanticMapper;
        this.searchSemanticSearchRepository = searchSemanticSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link SearchSemanticDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SearchSemanticDTO> findByCriteria(SearchSemanticCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SearchSemantic> specification = createSpecification(criteria);
        return searchSemanticRepository.findAll(specification, page).map(searchSemanticMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SearchSemanticCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<SearchSemantic> specification = createSpecification(criteria);
        return searchSemanticRepository.count(specification);
    }

    /**
     * Function to convert {@link SearchSemanticCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SearchSemantic> createSpecification(SearchSemanticCriteria criteria) {
        Specification<SearchSemantic> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), SearchSemantic_.id),
                buildStringSpecification(criteria.getQuery(), SearchSemantic_.query),
                buildStringSpecification(criteria.getModelUsed(), SearchSemantic_.modelUsed),
                buildRangeSpecification(criteria.getExecutionTime(), SearchSemantic_.executionTime),
                buildStringSpecification(criteria.getUserId(), SearchSemantic_.userId),
                buildRangeSpecification(criteria.getSearchDate(), SearchSemantic_.searchDate)
            );
        }
        return specification;
    }
}
