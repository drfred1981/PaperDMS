package fr.smartprod.paperdms.document.service;

import fr.smartprod.paperdms.document.domain.*; // for static metamodels
import fr.smartprod.paperdms.document.domain.Folder;
import fr.smartprod.paperdms.document.repository.FolderRepository;
import fr.smartprod.paperdms.document.service.criteria.FolderCriteria;
import fr.smartprod.paperdms.document.service.dto.FolderDTO;
import fr.smartprod.paperdms.document.service.mapper.FolderMapper;
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
 * Service for executing complex queries for {@link Folder} entities in the database.
 * The main input is a {@link FolderCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link FolderDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FolderQueryService extends QueryService<Folder> {

    private static final Logger LOG = LoggerFactory.getLogger(FolderQueryService.class);

    private final FolderRepository folderRepository;

    private final FolderMapper folderMapper;

    public FolderQueryService(FolderRepository folderRepository, FolderMapper folderMapper) {
        this.folderRepository = folderRepository;
        this.folderMapper = folderMapper;
    }

    /**
     * Return a {@link Page} of {@link FolderDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FolderDTO> findByCriteria(FolderCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Folder> specification = createSpecification(criteria);
        return folderRepository.findAll(specification, page).map(folderMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FolderCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Folder> specification = createSpecification(criteria);
        return folderRepository.count(specification);
    }

    /**
     * Function to convert {@link FolderCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Folder> createSpecification(FolderCriteria criteria) {
        Specification<Folder> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Folder_.id),
                buildStringSpecification(criteria.getName(), Folder_.name),
                buildStringSpecification(criteria.getPath(), Folder_.path),
                buildSpecification(criteria.getIsShared(), Folder_.isShared),
                buildRangeSpecification(criteria.getCreatedDate(), Folder_.createdDate),
                buildStringSpecification(criteria.getCreatedBy(), Folder_.createdBy),
                buildSpecification(criteria.getChildrenId(), root -> root.join(Folder_.children, JoinType.LEFT).get(Folder_.id)),
                buildSpecification(criteria.getParentId(), root -> root.join(Folder_.parent, JoinType.LEFT).get(Folder_.id))
            );
        }
        return specification;
    }
}
