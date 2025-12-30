package fr.smartprod.paperdms.document.service;

import fr.smartprod.paperdms.document.domain.*; // for static metamodels
import fr.smartprod.paperdms.document.domain.MetaSmartFolder;
import fr.smartprod.paperdms.document.repository.MetaSmartFolderRepository;
import fr.smartprod.paperdms.document.repository.search.MetaSmartFolderSearchRepository;
import fr.smartprod.paperdms.document.service.criteria.MetaSmartFolderCriteria;
import fr.smartprod.paperdms.document.service.dto.MetaSmartFolderDTO;
import fr.smartprod.paperdms.document.service.mapper.MetaSmartFolderMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link MetaSmartFolder} entities in the database.
 * The main input is a {@link MetaSmartFolderCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link MetaSmartFolderDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MetaSmartFolderQueryService extends QueryService<MetaSmartFolder> {

    private static final Logger LOG = LoggerFactory.getLogger(MetaSmartFolderQueryService.class);

    private final MetaSmartFolderRepository metaSmartFolderRepository;

    private final MetaSmartFolderMapper metaSmartFolderMapper;

    private final MetaSmartFolderSearchRepository metaSmartFolderSearchRepository;

    public MetaSmartFolderQueryService(
        MetaSmartFolderRepository metaSmartFolderRepository,
        MetaSmartFolderMapper metaSmartFolderMapper,
        MetaSmartFolderSearchRepository metaSmartFolderSearchRepository
    ) {
        this.metaSmartFolderRepository = metaSmartFolderRepository;
        this.metaSmartFolderMapper = metaSmartFolderMapper;
        this.metaSmartFolderSearchRepository = metaSmartFolderSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link MetaSmartFolderDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MetaSmartFolderDTO> findByCriteria(MetaSmartFolderCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MetaSmartFolder> specification = createSpecification(criteria);
        return metaSmartFolderRepository.findAll(specification, page).map(metaSmartFolderMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MetaSmartFolderCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<MetaSmartFolder> specification = createSpecification(criteria);
        return metaSmartFolderRepository.count(specification);
    }

    /**
     * Function to convert {@link MetaSmartFolderCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MetaSmartFolder> createSpecification(MetaSmartFolderCriteria criteria) {
        Specification<MetaSmartFolder> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), MetaSmartFolder_.id),
                buildStringSpecification(criteria.getName(), MetaSmartFolder_.name),
                buildSpecification(criteria.getAutoRefresh(), MetaSmartFolder_.autoRefresh),
                buildSpecification(criteria.getIsPublic(), MetaSmartFolder_.isPublic),
                buildStringSpecification(criteria.getCreatedBy(), MetaSmartFolder_.createdBy),
                buildRangeSpecification(criteria.getCreatedDate(), MetaSmartFolder_.createdDate)
            );
        }
        return specification;
    }
}
