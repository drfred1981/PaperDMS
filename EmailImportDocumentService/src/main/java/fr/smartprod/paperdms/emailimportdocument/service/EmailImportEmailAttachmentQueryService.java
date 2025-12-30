package fr.smartprod.paperdms.emailimportdocument.service;

import fr.smartprod.paperdms.emailimportdocument.domain.*; // for static metamodels
import fr.smartprod.paperdms.emailimportdocument.domain.EmailImportEmailAttachment;
import fr.smartprod.paperdms.emailimportdocument.repository.EmailImportEmailAttachmentRepository;
import fr.smartprod.paperdms.emailimportdocument.service.criteria.EmailImportEmailAttachmentCriteria;
import fr.smartprod.paperdms.emailimportdocument.service.dto.EmailImportEmailAttachmentDTO;
import fr.smartprod.paperdms.emailimportdocument.service.mapper.EmailImportEmailAttachmentMapper;
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
 * Service for executing complex queries for {@link EmailImportEmailAttachment} entities in the database.
 * The main input is a {@link EmailImportEmailAttachmentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link EmailImportEmailAttachmentDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EmailImportEmailAttachmentQueryService extends QueryService<EmailImportEmailAttachment> {

    private static final Logger LOG = LoggerFactory.getLogger(EmailImportEmailAttachmentQueryService.class);

    private final EmailImportEmailAttachmentRepository emailImportEmailAttachmentRepository;

    private final EmailImportEmailAttachmentMapper emailImportEmailAttachmentMapper;

    public EmailImportEmailAttachmentQueryService(
        EmailImportEmailAttachmentRepository emailImportEmailAttachmentRepository,
        EmailImportEmailAttachmentMapper emailImportEmailAttachmentMapper
    ) {
        this.emailImportEmailAttachmentRepository = emailImportEmailAttachmentRepository;
        this.emailImportEmailAttachmentMapper = emailImportEmailAttachmentMapper;
    }

    /**
     * Return a {@link Page} of {@link EmailImportEmailAttachmentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EmailImportEmailAttachmentDTO> findByCriteria(EmailImportEmailAttachmentCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EmailImportEmailAttachment> specification = createSpecification(criteria);
        return emailImportEmailAttachmentRepository.findAll(specification, page).map(emailImportEmailAttachmentMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EmailImportEmailAttachmentCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<EmailImportEmailAttachment> specification = createSpecification(criteria);
        return emailImportEmailAttachmentRepository.count(specification);
    }

    /**
     * Function to convert {@link EmailImportEmailAttachmentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EmailImportEmailAttachment> createSpecification(EmailImportEmailAttachmentCriteria criteria) {
        Specification<EmailImportEmailAttachment> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), EmailImportEmailAttachment_.id),
                buildStringSpecification(criteria.getFileName(), EmailImportEmailAttachment_.fileName),
                buildRangeSpecification(criteria.getFileSize(), EmailImportEmailAttachment_.fileSize),
                buildStringSpecification(criteria.getMimeType(), EmailImportEmailAttachment_.mimeType),
                buildStringSpecification(criteria.getSha256(), EmailImportEmailAttachment_.sha256),
                buildStringSpecification(criteria.gets3Key(), EmailImportEmailAttachment_.s3Key),
                buildSpecification(criteria.getStatus(), EmailImportEmailAttachment_.status),
                buildStringSpecification(criteria.getDocumentSha256(), EmailImportEmailAttachment_.documentSha256),
                buildSpecification(criteria.getEmailImportDocumentId(), root ->
                    root.join(EmailImportEmailAttachment_.emailImportDocument, JoinType.LEFT).get(EmailImportDocument_.id)
                )
            );
        }
        return specification;
    }
}
