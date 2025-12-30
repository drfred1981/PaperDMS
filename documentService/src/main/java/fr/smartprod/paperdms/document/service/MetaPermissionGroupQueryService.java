package fr.smartprod.paperdms.document.service;

import fr.smartprod.paperdms.document.domain.*; // for static metamodels
import fr.smartprod.paperdms.document.domain.MetaPermissionGroup;
import fr.smartprod.paperdms.document.repository.MetaPermissionGroupRepository;
import fr.smartprod.paperdms.document.repository.search.MetaPermissionGroupSearchRepository;
import fr.smartprod.paperdms.document.service.criteria.MetaPermissionGroupCriteria;
import fr.smartprod.paperdms.document.service.dto.MetaPermissionGroupDTO;
import fr.smartprod.paperdms.document.service.mapper.MetaPermissionGroupMapper;
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
 * Service for executing complex queries for {@link MetaPermissionGroup} entities in the database.
 * The main input is a {@link MetaPermissionGroupCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link MetaPermissionGroupDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MetaPermissionGroupQueryService extends QueryService<MetaPermissionGroup> {

    private static final Logger LOG = LoggerFactory.getLogger(MetaPermissionGroupQueryService.class);

    private final MetaPermissionGroupRepository metaPermissionGroupRepository;

    private final MetaPermissionGroupMapper metaPermissionGroupMapper;

    private final MetaPermissionGroupSearchRepository metaPermissionGroupSearchRepository;

    public MetaPermissionGroupQueryService(
        MetaPermissionGroupRepository metaPermissionGroupRepository,
        MetaPermissionGroupMapper metaPermissionGroupMapper,
        MetaPermissionGroupSearchRepository metaPermissionGroupSearchRepository
    ) {
        this.metaPermissionGroupRepository = metaPermissionGroupRepository;
        this.metaPermissionGroupMapper = metaPermissionGroupMapper;
        this.metaPermissionGroupSearchRepository = metaPermissionGroupSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link MetaPermissionGroupDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MetaPermissionGroupDTO> findByCriteria(MetaPermissionGroupCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MetaPermissionGroup> specification = createSpecification(criteria);
        return metaPermissionGroupRepository.findAll(specification, page).map(metaPermissionGroupMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MetaPermissionGroupCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<MetaPermissionGroup> specification = createSpecification(criteria);
        return metaPermissionGroupRepository.count(specification);
    }

    /**
     * Function to convert {@link MetaPermissionGroupCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MetaPermissionGroup> createSpecification(MetaPermissionGroupCriteria criteria) {
        Specification<MetaPermissionGroup> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), MetaPermissionGroup_.id),
                buildStringSpecification(criteria.getName(), MetaPermissionGroup_.name),
                buildSpecification(criteria.getIsSystem(), MetaPermissionGroup_.isSystem),
                buildRangeSpecification(criteria.getCreatedDate(), MetaPermissionGroup_.createdDate),
                buildStringSpecification(criteria.getCreatedBy(), MetaPermissionGroup_.createdBy),
                buildSpecification(criteria.getDocumentPermissionsId(), root ->
                    root.join(MetaPermissionGroup_.documentPermissions, JoinType.LEFT).get(DocumentPermission_.id)
                )
            );
        }
        return specification;
    }
}
