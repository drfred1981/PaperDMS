package fr.smartprod.paperdms.document.service;

import fr.smartprod.paperdms.document.domain.*; // for static metamodels
import fr.smartprod.paperdms.document.domain.TagCategory;
import fr.smartprod.paperdms.document.repository.TagCategoryRepository;
import fr.smartprod.paperdms.document.repository.search.TagCategorySearchRepository;
import fr.smartprod.paperdms.document.service.criteria.TagCategoryCriteria;
import fr.smartprod.paperdms.document.service.dto.TagCategoryDTO;
import fr.smartprod.paperdms.document.service.mapper.TagCategoryMapper;
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
 * Service for executing complex queries for {@link TagCategory} entities in the database.
 * The main input is a {@link TagCategoryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link TagCategoryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TagCategoryQueryService extends QueryService<TagCategory> {

    private static final Logger LOG = LoggerFactory.getLogger(TagCategoryQueryService.class);

    private final TagCategoryRepository tagCategoryRepository;

    private final TagCategoryMapper tagCategoryMapper;

    private final TagCategorySearchRepository tagCategorySearchRepository;

    public TagCategoryQueryService(
        TagCategoryRepository tagCategoryRepository,
        TagCategoryMapper tagCategoryMapper,
        TagCategorySearchRepository tagCategorySearchRepository
    ) {
        this.tagCategoryRepository = tagCategoryRepository;
        this.tagCategoryMapper = tagCategoryMapper;
        this.tagCategorySearchRepository = tagCategorySearchRepository;
    }

    /**
     * Return a {@link Page} of {@link TagCategoryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TagCategoryDTO> findByCriteria(TagCategoryCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TagCategory> specification = createSpecification(criteria);
        return tagCategoryRepository.findAll(specification, page).map(tagCategoryMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TagCategoryCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<TagCategory> specification = createSpecification(criteria);
        return tagCategoryRepository.count(specification);
    }

    /**
     * Function to convert {@link TagCategoryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TagCategory> createSpecification(TagCategoryCriteria criteria) {
        Specification<TagCategory> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), TagCategory_.id),
                buildStringSpecification(criteria.getName(), TagCategory_.name),
                buildStringSpecification(criteria.getColor(), TagCategory_.color),
                buildRangeSpecification(criteria.getDisplayOrder(), TagCategory_.displayOrder),
                buildSpecification(criteria.getIsSystem(), TagCategory_.isSystem),
                buildRangeSpecification(criteria.getCreatedDate(), TagCategory_.createdDate),
                buildStringSpecification(criteria.getCreatedBy(), TagCategory_.createdBy),
                buildSpecification(criteria.getChildrenId(), root -> root.join(TagCategory_.children, JoinType.LEFT).get(TagCategory_.id)),
                buildSpecification(criteria.getTagsId(), root -> root.join(TagCategory_.tags, JoinType.LEFT).get(Tag_.id)),
                buildSpecification(criteria.getParentId(), root -> root.join(TagCategory_.parent, JoinType.LEFT).get(TagCategory_.id))
            );
        }
        return specification;
    }
}
