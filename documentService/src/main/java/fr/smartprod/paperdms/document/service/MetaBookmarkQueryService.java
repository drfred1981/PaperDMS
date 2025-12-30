package fr.smartprod.paperdms.document.service;

import fr.smartprod.paperdms.document.domain.*; // for static metamodels
import fr.smartprod.paperdms.document.domain.MetaBookmark;
import fr.smartprod.paperdms.document.repository.MetaBookmarkRepository;
import fr.smartprod.paperdms.document.repository.search.MetaBookmarkSearchRepository;
import fr.smartprod.paperdms.document.service.criteria.MetaBookmarkCriteria;
import fr.smartprod.paperdms.document.service.dto.MetaBookmarkDTO;
import fr.smartprod.paperdms.document.service.mapper.MetaBookmarkMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link MetaBookmark} entities in the database.
 * The main input is a {@link MetaBookmarkCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link MetaBookmarkDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MetaBookmarkQueryService extends QueryService<MetaBookmark> {

    private static final Logger LOG = LoggerFactory.getLogger(MetaBookmarkQueryService.class);

    private final MetaBookmarkRepository metaBookmarkRepository;

    private final MetaBookmarkMapper metaBookmarkMapper;

    private final MetaBookmarkSearchRepository metaBookmarkSearchRepository;

    public MetaBookmarkQueryService(
        MetaBookmarkRepository metaBookmarkRepository,
        MetaBookmarkMapper metaBookmarkMapper,
        MetaBookmarkSearchRepository metaBookmarkSearchRepository
    ) {
        this.metaBookmarkRepository = metaBookmarkRepository;
        this.metaBookmarkMapper = metaBookmarkMapper;
        this.metaBookmarkSearchRepository = metaBookmarkSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link MetaBookmarkDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MetaBookmarkDTO> findByCriteria(MetaBookmarkCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MetaBookmark> specification = createSpecification(criteria);
        return metaBookmarkRepository.findAll(specification, page).map(metaBookmarkMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MetaBookmarkCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<MetaBookmark> specification = createSpecification(criteria);
        return metaBookmarkRepository.count(specification);
    }

    /**
     * Function to convert {@link MetaBookmarkCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MetaBookmark> createSpecification(MetaBookmarkCriteria criteria) {
        Specification<MetaBookmark> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), MetaBookmark_.id),
                buildStringSpecification(criteria.getUserId(), MetaBookmark_.userId),
                buildSpecification(criteria.getEntityType(), MetaBookmark_.entityType),
                buildStringSpecification(criteria.getEntityName(), MetaBookmark_.entityName),
                buildRangeSpecification(criteria.getCreatedDate(), MetaBookmark_.createdDate)
            );
        }
        return specification;
    }
}
