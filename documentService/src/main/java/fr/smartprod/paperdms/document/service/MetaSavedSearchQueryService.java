package fr.smartprod.paperdms.document.service;

import fr.smartprod.paperdms.document.domain.*; // for static metamodels
import fr.smartprod.paperdms.document.domain.MetaSavedSearch;
import fr.smartprod.paperdms.document.repository.MetaSavedSearchRepository;
import fr.smartprod.paperdms.document.repository.search.MetaSavedSearchSearchRepository;
import fr.smartprod.paperdms.document.service.criteria.MetaSavedSearchCriteria;
import fr.smartprod.paperdms.document.service.dto.MetaSavedSearchDTO;
import fr.smartprod.paperdms.document.service.mapper.MetaSavedSearchMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link MetaSavedSearch} entities in the database.
 * The main input is a {@link MetaSavedSearchCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link MetaSavedSearchDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MetaSavedSearchQueryService extends QueryService<MetaSavedSearch> {

    private static final Logger LOG = LoggerFactory.getLogger(MetaSavedSearchQueryService.class);

    private final MetaSavedSearchRepository metaSavedSearchRepository;

    private final MetaSavedSearchMapper metaSavedSearchMapper;

    private final MetaSavedSearchSearchRepository metaSavedSearchSearchRepository;

    public MetaSavedSearchQueryService(
        MetaSavedSearchRepository metaSavedSearchRepository,
        MetaSavedSearchMapper metaSavedSearchMapper,
        MetaSavedSearchSearchRepository metaSavedSearchSearchRepository
    ) {
        this.metaSavedSearchRepository = metaSavedSearchRepository;
        this.metaSavedSearchMapper = metaSavedSearchMapper;
        this.metaSavedSearchSearchRepository = metaSavedSearchSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link MetaSavedSearchDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MetaSavedSearchDTO> findByCriteria(MetaSavedSearchCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MetaSavedSearch> specification = createSpecification(criteria);
        return metaSavedSearchRepository.findAll(specification, page).map(metaSavedSearchMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MetaSavedSearchCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<MetaSavedSearch> specification = createSpecification(criteria);
        return metaSavedSearchRepository.count(specification);
    }

    /**
     * Function to convert {@link MetaSavedSearchCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MetaSavedSearch> createSpecification(MetaSavedSearchCriteria criteria) {
        Specification<MetaSavedSearch> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), MetaSavedSearch_.id),
                buildStringSpecification(criteria.getName(), MetaSavedSearch_.name),
                buildSpecification(criteria.getIsPublic(), MetaSavedSearch_.isPublic),
                buildSpecification(criteria.getIsAlert(), MetaSavedSearch_.isAlert),
                buildSpecification(criteria.getAlertFrequency(), MetaSavedSearch_.alertFrequency),
                buildStringSpecification(criteria.getUserId(), MetaSavedSearch_.userId),
                buildRangeSpecification(criteria.getCreatedDate(), MetaSavedSearch_.createdDate)
            );
        }
        return specification;
    }
}
