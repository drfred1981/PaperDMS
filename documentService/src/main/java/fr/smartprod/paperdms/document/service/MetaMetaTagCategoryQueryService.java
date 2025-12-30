package fr.smartprod.paperdms.document.service;

import fr.smartprod.paperdms.document.domain.*; // for static metamodels
import fr.smartprod.paperdms.document.domain.MetaMetaTagCategory;
import fr.smartprod.paperdms.document.repository.MetaMetaTagCategoryRepository;
import fr.smartprod.paperdms.document.repository.search.MetaMetaTagCategorySearchRepository;
import fr.smartprod.paperdms.document.service.criteria.MetaMetaTagCategoryCriteria;
import fr.smartprod.paperdms.document.service.dto.MetaMetaTagCategoryDTO;
import fr.smartprod.paperdms.document.service.mapper.MetaMetaTagCategoryMapper;
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
 * Service for executing complex queries for {@link MetaMetaTagCategory} entities in the database.
 * The main input is a {@link MetaMetaTagCategoryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link MetaMetaTagCategoryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MetaMetaTagCategoryQueryService extends QueryService<MetaMetaTagCategory> {

    private static final Logger LOG = LoggerFactory.getLogger(MetaMetaTagCategoryQueryService.class);

    private final MetaMetaTagCategoryRepository metaMetaTagCategoryRepository;

    private final MetaMetaTagCategoryMapper metaMetaTagCategoryMapper;

    private final MetaMetaTagCategorySearchRepository metaMetaTagCategorySearchRepository;

    public MetaMetaTagCategoryQueryService(
        MetaMetaTagCategoryRepository metaMetaTagCategoryRepository,
        MetaMetaTagCategoryMapper metaMetaTagCategoryMapper,
        MetaMetaTagCategorySearchRepository metaMetaTagCategorySearchRepository
    ) {
        this.metaMetaTagCategoryRepository = metaMetaTagCategoryRepository;
        this.metaMetaTagCategoryMapper = metaMetaTagCategoryMapper;
        this.metaMetaTagCategorySearchRepository = metaMetaTagCategorySearchRepository;
    }

    /**
     * Return a {@link Page} of {@link MetaMetaTagCategoryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MetaMetaTagCategoryDTO> findByCriteria(MetaMetaTagCategoryCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MetaMetaTagCategory> specification = createSpecification(criteria);
        return metaMetaTagCategoryRepository.findAll(specification, page).map(metaMetaTagCategoryMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MetaMetaTagCategoryCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<MetaMetaTagCategory> specification = createSpecification(criteria);
        return metaMetaTagCategoryRepository.count(specification);
    }

    /**
     * Function to convert {@link MetaMetaTagCategoryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MetaMetaTagCategory> createSpecification(MetaMetaTagCategoryCriteria criteria) {
        Specification<MetaMetaTagCategory> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), MetaMetaTagCategory_.id),
                buildStringSpecification(criteria.getName(), MetaMetaTagCategory_.name),
                buildStringSpecification(criteria.getColor(), MetaMetaTagCategory_.color),
                buildRangeSpecification(criteria.getDisplayOrder(), MetaMetaTagCategory_.displayOrder),
                buildSpecification(criteria.getIsSystem(), MetaMetaTagCategory_.isSystem),
                buildRangeSpecification(criteria.getCreatedDate(), MetaMetaTagCategory_.createdDate),
                buildStringSpecification(criteria.getCreatedBy(), MetaMetaTagCategory_.createdBy),
                buildSpecification(criteria.getChildrenId(), root ->
                    root.join(MetaMetaTagCategory_.children, JoinType.LEFT).get(MetaMetaTagCategory_.id)
                ),
                buildSpecification(criteria.getMetaTagsId(), root ->
                    root.join(MetaMetaTagCategory_.metaTags, JoinType.LEFT).get(MetaTag_.id)
                ),
                buildSpecification(criteria.getParentId(), root ->
                    root.join(MetaMetaTagCategory_.parent, JoinType.LEFT).get(MetaMetaTagCategory_.id)
                )
            );
        }
        return specification;
    }
}
