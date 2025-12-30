package fr.smartprod.paperdms.document.service;

import fr.smartprod.paperdms.document.domain.*; // for static metamodels
import fr.smartprod.paperdms.document.domain.MetaTag;
import fr.smartprod.paperdms.document.repository.MetaTagRepository;
import fr.smartprod.paperdms.document.repository.search.MetaTagSearchRepository;
import fr.smartprod.paperdms.document.service.criteria.MetaTagCriteria;
import fr.smartprod.paperdms.document.service.dto.MetaTagDTO;
import fr.smartprod.paperdms.document.service.mapper.MetaTagMapper;
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
 * Service for executing complex queries for {@link MetaTag} entities in the database.
 * The main input is a {@link MetaTagCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link MetaTagDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MetaTagQueryService extends QueryService<MetaTag> {

    private static final Logger LOG = LoggerFactory.getLogger(MetaTagQueryService.class);

    private final MetaTagRepository metaTagRepository;

    private final MetaTagMapper metaTagMapper;

    private final MetaTagSearchRepository metaTagSearchRepository;

    public MetaTagQueryService(
        MetaTagRepository metaTagRepository,
        MetaTagMapper metaTagMapper,
        MetaTagSearchRepository metaTagSearchRepository
    ) {
        this.metaTagRepository = metaTagRepository;
        this.metaTagMapper = metaTagMapper;
        this.metaTagSearchRepository = metaTagSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link MetaTagDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MetaTagDTO> findByCriteria(MetaTagCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MetaTag> specification = createSpecification(criteria);
        return metaTagRepository.findAll(specification, page).map(metaTagMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MetaTagCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<MetaTag> specification = createSpecification(criteria);
        return metaTagRepository.count(specification);
    }

    /**
     * Function to convert {@link MetaTagCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MetaTag> createSpecification(MetaTagCriteria criteria) {
        Specification<MetaTag> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), MetaTag_.id),
                buildStringSpecification(criteria.getName(), MetaTag_.name),
                buildStringSpecification(criteria.getColor(), MetaTag_.color),
                buildStringSpecification(criteria.getDescription(), MetaTag_.description),
                buildRangeSpecification(criteria.getUsageCount(), MetaTag_.usageCount),
                buildSpecification(criteria.getIsSystem(), MetaTag_.isSystem),
                buildRangeSpecification(criteria.getCreatedDate(), MetaTag_.createdDate),
                buildStringSpecification(criteria.getCreatedBy(), MetaTag_.createdBy),
                buildSpecification(criteria.getDocumentTagsId(), root ->
                    root.join(MetaTag_.documentTags, JoinType.LEFT).get(DocumentTag_.id)
                ),
                buildSpecification(criteria.getMetaMetaTagCategoryId(), root ->
                    root.join(MetaTag_.metaMetaTagCategory, JoinType.LEFT).get(MetaMetaTagCategory_.id)
                )
            );
        }
        return specification;
    }
}
