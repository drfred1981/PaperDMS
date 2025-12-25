package fr.smartprod.paperdms.document.service;

import fr.smartprod.paperdms.document.domain.*; // for static metamodels
import fr.smartprod.paperdms.document.domain.Document;
import fr.smartprod.paperdms.document.repository.DocumentRepository;
import fr.smartprod.paperdms.document.repository.search.DocumentSearchRepository;
import fr.smartprod.paperdms.document.service.criteria.DocumentCriteria;
import fr.smartprod.paperdms.document.service.dto.DocumentDTO;
import fr.smartprod.paperdms.document.service.mapper.DocumentMapper;
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
 * Service for executing complex queries for {@link Document} entities in the database.
 * The main input is a {@link DocumentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link DocumentDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DocumentQueryService extends QueryService<Document> {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentQueryService.class);

    private final DocumentRepository documentRepository;

    private final DocumentMapper documentMapper;

    private final DocumentSearchRepository documentSearchRepository;

    public DocumentQueryService(
        DocumentRepository documentRepository,
        DocumentMapper documentMapper,
        DocumentSearchRepository documentSearchRepository
    ) {
        this.documentRepository = documentRepository;
        this.documentMapper = documentMapper;
        this.documentSearchRepository = documentSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link DocumentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DocumentDTO> findByCriteria(DocumentCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Document> specification = createSpecification(criteria);
        return documentRepository.findAll(specification, page).map(documentMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DocumentCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Document> specification = createSpecification(criteria);
        return documentRepository.count(specification);
    }

    /**
     * Function to convert {@link DocumentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Document> createSpecification(DocumentCriteria criteria) {
        Specification<Document> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Document_.id),
                buildStringSpecification(criteria.getTitle(), Document_.title),
                buildStringSpecification(criteria.getFileName(), Document_.fileName),
                buildRangeSpecification(criteria.getFileSize(), Document_.fileSize),
                buildStringSpecification(criteria.getMimeType(), Document_.mimeType),
                buildStringSpecification(criteria.getSha256(), Document_.sha256),
                buildStringSpecification(criteria.gets3Key(), Document_.s3Key),
                buildStringSpecification(criteria.gets3Bucket(), Document_.s3Bucket),
                buildStringSpecification(criteria.gets3Region(), Document_.s3Region),
                buildStringSpecification(criteria.gets3Etag(), Document_.s3Etag),
                buildStringSpecification(criteria.getThumbnailS3Key(), Document_.thumbnailS3Key),
                buildStringSpecification(criteria.getThumbnailSha256(), Document_.thumbnailSha256),
                buildStringSpecification(criteria.getWebpPreviewS3Key(), Document_.webpPreviewS3Key),
                buildStringSpecification(criteria.getWebpPreviewSha256(), Document_.webpPreviewSha256),
                buildRangeSpecification(criteria.getUploadDate(), Document_.uploadDate),
                buildSpecification(criteria.getIsPublic(), Document_.isPublic),
                buildRangeSpecification(criteria.getDownloadCount(), Document_.downloadCount),
                buildRangeSpecification(criteria.getViewCount(), Document_.viewCount),
                buildStringSpecification(criteria.getDetectedLanguage(), Document_.detectedLanguage),
                buildStringSpecification(criteria.getManualLanguage(), Document_.manualLanguage),
                buildRangeSpecification(criteria.getLanguageConfidence(), Document_.languageConfidence),
                buildRangeSpecification(criteria.getPageCount(), Document_.pageCount),
                buildRangeSpecification(criteria.getCreatedDate(), Document_.createdDate),
                buildStringSpecification(criteria.getCreatedBy(), Document_.createdBy),
                buildSpecification(criteria.getFolderId(), root -> root.join(Document_.folder, JoinType.LEFT).get(Folder_.id)),
                buildSpecification(criteria.getDocumentTypeId(), root ->
                    root.join(Document_.documentType, JoinType.LEFT).get(DocumentType_.id)
                )
            );
        }
        return specification;
    }
}
