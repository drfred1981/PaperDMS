package fr.smartprod.paperdms.emailimportdocument.service;

import fr.smartprod.paperdms.emailimportdocument.domain.*; // for static metamodels
import fr.smartprod.paperdms.emailimportdocument.domain.EmailImportDocument;
import fr.smartprod.paperdms.emailimportdocument.repository.EmailImportDocumentRepository;
import fr.smartprod.paperdms.emailimportdocument.service.criteria.EmailImportDocumentCriteria;
import fr.smartprod.paperdms.emailimportdocument.service.dto.EmailImportDocumentDTO;
import fr.smartprod.paperdms.emailimportdocument.service.mapper.EmailImportDocumentMapper;
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
 * Service for executing complex queries for {@link EmailImportDocument} entities in the database.
 * The main input is a {@link EmailImportDocumentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link EmailImportDocumentDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EmailImportDocumentQueryService extends QueryService<EmailImportDocument> {

    private static final Logger LOG = LoggerFactory.getLogger(EmailImportDocumentQueryService.class);

    private final EmailImportDocumentRepository emailImportDocumentRepository;

    private final EmailImportDocumentMapper emailImportDocumentMapper;

    public EmailImportDocumentQueryService(
        EmailImportDocumentRepository emailImportDocumentRepository,
        EmailImportDocumentMapper emailImportDocumentMapper
    ) {
        this.emailImportDocumentRepository = emailImportDocumentRepository;
        this.emailImportDocumentMapper = emailImportDocumentMapper;
    }

    /**
     * Return a {@link Page} of {@link EmailImportDocumentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EmailImportDocumentDTO> findByCriteria(EmailImportDocumentCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EmailImportDocument> specification = createSpecification(criteria);
        return emailImportDocumentRepository.findAll(specification, page).map(emailImportDocumentMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EmailImportDocumentCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<EmailImportDocument> specification = createSpecification(criteria);
        return emailImportDocumentRepository.count(specification);
    }

    /**
     * Function to convert {@link EmailImportDocumentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EmailImportDocument> createSpecification(EmailImportDocumentCriteria criteria) {
        Specification<EmailImportDocument> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), EmailImportDocument_.id),
                buildStringSpecification(criteria.getSha256(), EmailImportDocument_.sha256),
                buildStringSpecification(criteria.getFromEmail(), EmailImportDocument_.fromEmail),
                buildStringSpecification(criteria.getToEmail(), EmailImportDocument_.toEmail),
                buildStringSpecification(criteria.getSubject(), EmailImportDocument_.subject),
                buildRangeSpecification(criteria.getReceivedDate(), EmailImportDocument_.receivedDate),
                buildRangeSpecification(criteria.getProcessedDate(), EmailImportDocument_.processedDate),
                buildSpecification(criteria.getStatus(), EmailImportDocument_.status),
                buildRangeSpecification(criteria.getAttachmentCount(), EmailImportDocument_.attachmentCount),
                buildRangeSpecification(criteria.getDocumentsCreated(), EmailImportDocument_.documentsCreated),
                buildStringSpecification(criteria.getDocumentSha256(), EmailImportDocument_.documentSha256),
                buildSpecification(criteria.getEmailImportEmailAttachmentsId(), root ->
                    root.join(EmailImportDocument_.emailImportEmailAttachments, JoinType.LEFT).get(EmailImportEmailAttachment_.id)
                ),
                buildSpecification(criteria.getAppliedRuleId(), root ->
                    root.join(EmailImportDocument_.appliedRule, JoinType.LEFT).get(EmailImportImportRule_.id)
                )
            );
        }
        return specification;
    }
}
