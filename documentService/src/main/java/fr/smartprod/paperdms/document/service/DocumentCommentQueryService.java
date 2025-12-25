package fr.smartprod.paperdms.document.service;

import fr.smartprod.paperdms.document.domain.*; // for static metamodels
import fr.smartprod.paperdms.document.domain.DocumentComment;
import fr.smartprod.paperdms.document.repository.DocumentCommentRepository;
import fr.smartprod.paperdms.document.service.criteria.DocumentCommentCriteria;
import fr.smartprod.paperdms.document.service.dto.DocumentCommentDTO;
import fr.smartprod.paperdms.document.service.mapper.DocumentCommentMapper;
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
 * Service for executing complex queries for {@link DocumentComment} entities in the database.
 * The main input is a {@link DocumentCommentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link DocumentCommentDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DocumentCommentQueryService extends QueryService<DocumentComment> {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentCommentQueryService.class);

    private final DocumentCommentRepository documentCommentRepository;

    private final DocumentCommentMapper documentCommentMapper;

    public DocumentCommentQueryService(DocumentCommentRepository documentCommentRepository, DocumentCommentMapper documentCommentMapper) {
        this.documentCommentRepository = documentCommentRepository;
        this.documentCommentMapper = documentCommentMapper;
    }

    /**
     * Return a {@link Page} of {@link DocumentCommentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DocumentCommentDTO> findByCriteria(DocumentCommentCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DocumentComment> specification = createSpecification(criteria);
        return documentCommentRepository.findAll(specification, page).map(documentCommentMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DocumentCommentCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<DocumentComment> specification = createSpecification(criteria);
        return documentCommentRepository.count(specification);
    }

    /**
     * Function to convert {@link DocumentCommentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DocumentComment> createSpecification(DocumentCommentCriteria criteria) {
        Specification<DocumentComment> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), DocumentComment_.id),
                buildRangeSpecification(criteria.getDocumentId(), DocumentComment_.documentId),
                buildRangeSpecification(criteria.getPageNumber(), DocumentComment_.pageNumber),
                buildSpecification(criteria.getIsResolved(), DocumentComment_.isResolved),
                buildStringSpecification(criteria.getAuthorId(), DocumentComment_.authorId),
                buildRangeSpecification(criteria.getCreatedDate(), DocumentComment_.createdDate),
                buildSpecification(criteria.getRepliesId(), root ->
                    root.join(DocumentComment_.replies, JoinType.LEFT).get(DocumentComment_.id)
                ),
                buildSpecification(criteria.getParentCommentId(), root ->
                    root.join(DocumentComment_.parentComment, JoinType.LEFT).get(DocumentComment_.id)
                )
            );
        }
        return specification;
    }
}
