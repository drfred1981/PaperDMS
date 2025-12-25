package fr.smartprod.paperdms.document.service;

import fr.smartprod.paperdms.document.domain.*; // for static metamodels
import fr.smartprod.paperdms.document.domain.DocumentAudit;
import fr.smartprod.paperdms.document.repository.DocumentAuditRepository;
import fr.smartprod.paperdms.document.service.criteria.DocumentAuditCriteria;
import fr.smartprod.paperdms.document.service.dto.DocumentAuditDTO;
import fr.smartprod.paperdms.document.service.mapper.DocumentAuditMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link DocumentAudit} entities in the database.
 * The main input is a {@link DocumentAuditCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link DocumentAuditDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DocumentAuditQueryService extends QueryService<DocumentAudit> {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentAuditQueryService.class);

    private final DocumentAuditRepository documentAuditRepository;

    private final DocumentAuditMapper documentAuditMapper;

    public DocumentAuditQueryService(DocumentAuditRepository documentAuditRepository, DocumentAuditMapper documentAuditMapper) {
        this.documentAuditRepository = documentAuditRepository;
        this.documentAuditMapper = documentAuditMapper;
    }

    /**
     * Return a {@link Page} of {@link DocumentAuditDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DocumentAuditDTO> findByCriteria(DocumentAuditCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DocumentAudit> specification = createSpecification(criteria);
        return documentAuditRepository.findAll(specification, page).map(documentAuditMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DocumentAuditCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<DocumentAudit> specification = createSpecification(criteria);
        return documentAuditRepository.count(specification);
    }

    /**
     * Function to convert {@link DocumentAuditCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DocumentAudit> createSpecification(DocumentAuditCriteria criteria) {
        Specification<DocumentAudit> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), DocumentAudit_.id),
                buildRangeSpecification(criteria.getDocumentId(), DocumentAudit_.documentId),
                buildStringSpecification(criteria.getDocumentSha256(), DocumentAudit_.documentSha256),
                buildSpecification(criteria.getAction(), DocumentAudit_.action),
                buildStringSpecification(criteria.getUserId(), DocumentAudit_.userId),
                buildStringSpecification(criteria.getUserIp(), DocumentAudit_.userIp),
                buildRangeSpecification(criteria.getActionDate(), DocumentAudit_.actionDate)
            );
        }
        return specification;
    }
}
