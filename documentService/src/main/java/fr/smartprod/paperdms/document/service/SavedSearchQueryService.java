package fr.smartprod.paperdms.document.service;

import fr.smartprod.paperdms.document.domain.*; // for static metamodels
import fr.smartprod.paperdms.document.domain.SavedSearch;
import fr.smartprod.paperdms.document.repository.SavedSearchRepository;
import fr.smartprod.paperdms.document.service.criteria.SavedSearchCriteria;
import fr.smartprod.paperdms.document.service.dto.SavedSearchDTO;
import fr.smartprod.paperdms.document.service.mapper.SavedSearchMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link SavedSearch} entities in the database.
 * The main input is a {@link SavedSearchCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link SavedSearchDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SavedSearchQueryService extends QueryService<SavedSearch> {

    private static final Logger LOG = LoggerFactory.getLogger(SavedSearchQueryService.class);

    private final SavedSearchRepository savedSearchRepository;

    private final SavedSearchMapper savedSearchMapper;

    public SavedSearchQueryService(SavedSearchRepository savedSearchRepository, SavedSearchMapper savedSearchMapper) {
        this.savedSearchRepository = savedSearchRepository;
        this.savedSearchMapper = savedSearchMapper;
    }

    /**
     * Return a {@link Page} of {@link SavedSearchDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SavedSearchDTO> findByCriteria(SavedSearchCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SavedSearch> specification = createSpecification(criteria);
        return savedSearchRepository.findAll(specification, page).map(savedSearchMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SavedSearchCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<SavedSearch> specification = createSpecification(criteria);
        return savedSearchRepository.count(specification);
    }

    /**
     * Function to convert {@link SavedSearchCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SavedSearch> createSpecification(SavedSearchCriteria criteria) {
        Specification<SavedSearch> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), SavedSearch_.id),
                buildStringSpecification(criteria.getName(), SavedSearch_.name),
                buildSpecification(criteria.getIsPublic(), SavedSearch_.isPublic),
                buildSpecification(criteria.getIsAlert(), SavedSearch_.isAlert),
                buildSpecification(criteria.getAlertFrequency(), SavedSearch_.alertFrequency),
                buildStringSpecification(criteria.getUserId(), SavedSearch_.userId),
                buildRangeSpecification(criteria.getCreatedDate(), SavedSearch_.createdDate)
            );
        }
        return specification;
    }
}
