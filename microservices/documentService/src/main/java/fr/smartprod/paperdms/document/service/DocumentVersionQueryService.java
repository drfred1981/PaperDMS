package fr.smartprod.paperdms.document.service;

import fr.smartprod.paperdms.document.domain.*; // for static metamodels
import fr.smartprod.paperdms.document.domain.DocumentVersion;
import fr.smartprod.paperdms.document.repository.DocumentVersionRepository;
import fr.smartprod.paperdms.document.repository.search.DocumentVersionSearchRepository;
import fr.smartprod.paperdms.document.service.criteria.DocumentVersionCriteria;
import fr.smartprod.paperdms.document.service.dto.DocumentVersionDTO;
import fr.smartprod.paperdms.document.service.mapper.DocumentVersionMapper;
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
 * Service for executing complex queries for {@link DocumentVersion} entities in the database.
 * The main input is a {@link DocumentVersionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link DocumentVersionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DocumentVersionQueryService extends QueryService<DocumentVersion> {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentVersionQueryService.class);

    private final DocumentVersionRepository documentVersionRepository;

    private final DocumentVersionMapper documentVersionMapper;

    private final DocumentVersionSearchRepository documentVersionSearchRepository;

    public DocumentVersionQueryService(
        DocumentVersionRepository documentVersionRepository,
        DocumentVersionMapper documentVersionMapper,
        DocumentVersionSearchRepository documentVersionSearchRepository
    ) {
        this.documentVersionRepository = documentVersionRepository;
        this.documentVersionMapper = documentVersionMapper;
        this.documentVersionSearchRepository = documentVersionSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link DocumentVersionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DocumentVersionDTO> findByCriteria(DocumentVersionCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DocumentVersion> specification = createSpecification(criteria);
        return documentVersionRepository.findAll(specification, page).map(documentVersionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DocumentVersionCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<DocumentVersion> specification = createSpecification(criteria);
        return documentVersionRepository.count(specification);
    }

    /**
     * Function to convert {@link DocumentVersionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DocumentVersion> createSpecification(DocumentVersionCriteria criteria) {
        Specification<DocumentVersion> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), DocumentVersion_.id),
                buildRangeSpecification(criteria.getVersionNumber(), DocumentVersion_.versionNumber),
                buildStringSpecification(criteria.getSha256(), DocumentVersion_.sha256),
                buildStringSpecification(criteria.gets3Key(), DocumentVersion_.s3Key),
                buildRangeSpecification(criteria.getFileSize(), DocumentVersion_.fileSize),
                buildRangeSpecification(criteria.getUploadDate(), DocumentVersion_.uploadDate),
                buildSpecification(criteria.getIsActive(), DocumentVersion_.isActive),
                buildStringSpecification(criteria.getCreatedBy(), DocumentVersion_.createdBy),
                buildSpecification(criteria.getDocumentId(), root -> root.join(DocumentVersion_.document, JoinType.LEFT).get(Document_.id))
            );
        }
        return specification;
    }
}
