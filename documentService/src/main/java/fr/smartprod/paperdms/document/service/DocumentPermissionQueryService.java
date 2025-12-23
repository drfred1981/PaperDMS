package fr.smartprod.paperdms.document.service;

import fr.smartprod.paperdms.document.domain.*; // for static metamodels
import fr.smartprod.paperdms.document.domain.DocumentPermission;
import fr.smartprod.paperdms.document.repository.DocumentPermissionRepository;
import fr.smartprod.paperdms.document.service.criteria.DocumentPermissionCriteria;
import fr.smartprod.paperdms.document.service.dto.DocumentPermissionDTO;
import fr.smartprod.paperdms.document.service.mapper.DocumentPermissionMapper;
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
 * Service for executing complex queries for {@link DocumentPermission} entities in the database.
 * The main input is a {@link DocumentPermissionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link DocumentPermissionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DocumentPermissionQueryService extends QueryService<DocumentPermission> {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentPermissionQueryService.class);

    private final DocumentPermissionRepository documentPermissionRepository;

    private final DocumentPermissionMapper documentPermissionMapper;

    public DocumentPermissionQueryService(
        DocumentPermissionRepository documentPermissionRepository,
        DocumentPermissionMapper documentPermissionMapper
    ) {
        this.documentPermissionRepository = documentPermissionRepository;
        this.documentPermissionMapper = documentPermissionMapper;
    }

    /**
     * Return a {@link Page} of {@link DocumentPermissionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DocumentPermissionDTO> findByCriteria(DocumentPermissionCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DocumentPermission> specification = createSpecification(criteria);
        return documentPermissionRepository.findAll(specification, page).map(documentPermissionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DocumentPermissionCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<DocumentPermission> specification = createSpecification(criteria);
        return documentPermissionRepository.count(specification);
    }

    /**
     * Function to convert {@link DocumentPermissionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DocumentPermission> createSpecification(DocumentPermissionCriteria criteria) {
        Specification<DocumentPermission> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), DocumentPermission_.id),
                buildRangeSpecification(criteria.getDocumentId(), DocumentPermission_.documentId),
                buildSpecification(criteria.getPrincipalType(), DocumentPermission_.principalType),
                buildStringSpecification(criteria.getPrincipalId(), DocumentPermission_.principalId),
                buildSpecification(criteria.getPermission(), DocumentPermission_.permission),
                buildSpecification(criteria.getCanDelegate(), DocumentPermission_.canDelegate),
                buildStringSpecification(criteria.getGrantedBy(), DocumentPermission_.grantedBy),
                buildRangeSpecification(criteria.getGrantedDate(), DocumentPermission_.grantedDate),
                buildSpecification(criteria.getPermissionGroupId(), root ->
                    root.join(DocumentPermission_.permissionGroup, JoinType.LEFT).get(PermissionGroup_.id)
                )
            );
        }
        return specification;
    }
}
