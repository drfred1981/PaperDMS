package fr.smartprod.paperdms.document.service;

import fr.smartprod.paperdms.document.domain.*; // for static metamodels
import fr.smartprod.paperdms.document.domain.MetaFolder;
import fr.smartprod.paperdms.document.repository.MetaFolderRepository;
import fr.smartprod.paperdms.document.repository.search.MetaFolderSearchRepository;
import fr.smartprod.paperdms.document.service.criteria.MetaFolderCriteria;
import fr.smartprod.paperdms.document.service.dto.MetaFolderDTO;
import fr.smartprod.paperdms.document.service.mapper.MetaFolderMapper;
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
 * Service for executing complex queries for {@link MetaFolder} entities in the database.
 * The main input is a {@link MetaFolderCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link MetaFolderDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MetaFolderQueryService extends QueryService<MetaFolder> {

    private static final Logger LOG = LoggerFactory.getLogger(MetaFolderQueryService.class);

    private final MetaFolderRepository metaFolderRepository;

    private final MetaFolderMapper metaFolderMapper;

    private final MetaFolderSearchRepository metaFolderSearchRepository;

    public MetaFolderQueryService(
        MetaFolderRepository metaFolderRepository,
        MetaFolderMapper metaFolderMapper,
        MetaFolderSearchRepository metaFolderSearchRepository
    ) {
        this.metaFolderRepository = metaFolderRepository;
        this.metaFolderMapper = metaFolderMapper;
        this.metaFolderSearchRepository = metaFolderSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link MetaFolderDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MetaFolderDTO> findByCriteria(MetaFolderCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MetaFolder> specification = createSpecification(criteria);
        return metaFolderRepository.findAll(specification, page).map(metaFolderMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MetaFolderCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<MetaFolder> specification = createSpecification(criteria);
        return metaFolderRepository.count(specification);
    }

    /**
     * Function to convert {@link MetaFolderCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MetaFolder> createSpecification(MetaFolderCriteria criteria) {
        Specification<MetaFolder> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), MetaFolder_.id),
                buildStringSpecification(criteria.getName(), MetaFolder_.name),
                buildStringSpecification(criteria.getPath(), MetaFolder_.path),
                buildSpecification(criteria.getIsShared(), MetaFolder_.isShared),
                buildRangeSpecification(criteria.getCreatedDate(), MetaFolder_.createdDate),
                buildStringSpecification(criteria.getCreatedBy(), MetaFolder_.createdBy),
                buildSpecification(criteria.getChildrenId(), root -> root.join(MetaFolder_.children, JoinType.LEFT).get(MetaFolder_.id)),
                buildSpecification(criteria.getDocumentsId(), root -> root.join(MetaFolder_.documents, JoinType.LEFT).get(Document_.id)),
                buildSpecification(criteria.getParentId(), root -> root.join(MetaFolder_.parent, JoinType.LEFT).get(MetaFolder_.id))
            );
        }
        return specification;
    }
}
