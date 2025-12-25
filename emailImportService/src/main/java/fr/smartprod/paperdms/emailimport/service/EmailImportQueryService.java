package fr.smartprod.paperdms.emailimport.service;

import fr.smartprod.paperdms.emailimport.domain.*; // for static metamodels
import fr.smartprod.paperdms.emailimport.domain.EmailImport;
import fr.smartprod.paperdms.emailimport.repository.EmailImportRepository;
import fr.smartprod.paperdms.emailimport.service.criteria.EmailImportCriteria;
import fr.smartprod.paperdms.emailimport.service.dto.EmailImportDTO;
import fr.smartprod.paperdms.emailimport.service.mapper.EmailImportMapper;
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
 * Service for executing complex queries for {@link EmailImport} entities in the database.
 * The main input is a {@link EmailImportCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link EmailImportDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EmailImportQueryService extends QueryService<EmailImport> {

    private static final Logger LOG = LoggerFactory.getLogger(EmailImportQueryService.class);

    private final EmailImportRepository emailImportRepository;

    private final EmailImportMapper emailImportMapper;

    public EmailImportQueryService(EmailImportRepository emailImportRepository, EmailImportMapper emailImportMapper) {
        this.emailImportRepository = emailImportRepository;
        this.emailImportMapper = emailImportMapper;
    }

    /**
     * Return a {@link Page} of {@link EmailImportDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EmailImportDTO> findByCriteria(EmailImportCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EmailImport> specification = createSpecification(criteria);
        return emailImportRepository.findAll(specification, page).map(emailImportMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EmailImportCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<EmailImport> specification = createSpecification(criteria);
        return emailImportRepository.count(specification);
    }

    /**
     * Function to convert {@link EmailImportCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EmailImport> createSpecification(EmailImportCriteria criteria) {
        Specification<EmailImport> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), EmailImport_.id),
                buildStringSpecification(criteria.getFromEmail(), EmailImport_.fromEmail),
                buildStringSpecification(criteria.getToEmail(), EmailImport_.toEmail),
                buildStringSpecification(criteria.getSubject(), EmailImport_.subject),
                buildRangeSpecification(criteria.getReceivedDate(), EmailImport_.receivedDate),
                buildRangeSpecification(criteria.getProcessedDate(), EmailImport_.processedDate),
                buildSpecification(criteria.getStatus(), EmailImport_.status),
                buildRangeSpecification(criteria.getFolderId(), EmailImport_.folderId),
                buildRangeSpecification(criteria.getDocumentTypeId(), EmailImport_.documentTypeId),
                buildRangeSpecification(criteria.getAttachmentCount(), EmailImport_.attachmentCount),
                buildRangeSpecification(criteria.getDocumentsCreated(), EmailImport_.documentsCreated),
                buildRangeSpecification(criteria.getAppliedRuleId(), EmailImport_.appliedRuleId),
                buildSpecification(criteria.getAppliedRuleId(), root ->
                    root.join(EmailImport_.appliedRule, JoinType.LEFT).get(ImportRule_.id)
                )
            );
        }
        return specification;
    }
}
