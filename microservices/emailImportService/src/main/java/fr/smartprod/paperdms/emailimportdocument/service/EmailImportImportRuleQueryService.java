package fr.smartprod.paperdms.emailimportdocument.service;

import fr.smartprod.paperdms.emailimportdocument.domain.*; // for static metamodels
import fr.smartprod.paperdms.emailimportdocument.domain.EmailImportImportRule;
import fr.smartprod.paperdms.emailimportdocument.repository.EmailImportImportRuleRepository;
import fr.smartprod.paperdms.emailimportdocument.service.criteria.EmailImportImportRuleCriteria;
import fr.smartprod.paperdms.emailimportdocument.service.dto.EmailImportImportRuleDTO;
import fr.smartprod.paperdms.emailimportdocument.service.mapper.EmailImportImportRuleMapper;
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
 * Service for executing complex queries for {@link EmailImportImportRule} entities in the database.
 * The main input is a {@link EmailImportImportRuleCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link EmailImportImportRuleDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EmailImportImportRuleQueryService extends QueryService<EmailImportImportRule> {

    private static final Logger LOG = LoggerFactory.getLogger(EmailImportImportRuleQueryService.class);

    private final EmailImportImportRuleRepository emailImportImportRuleRepository;

    private final EmailImportImportRuleMapper emailImportImportRuleMapper;

    public EmailImportImportRuleQueryService(
        EmailImportImportRuleRepository emailImportImportRuleRepository,
        EmailImportImportRuleMapper emailImportImportRuleMapper
    ) {
        this.emailImportImportRuleRepository = emailImportImportRuleRepository;
        this.emailImportImportRuleMapper = emailImportImportRuleMapper;
    }

    /**
     * Return a {@link Page} of {@link EmailImportImportRuleDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EmailImportImportRuleDTO> findByCriteria(EmailImportImportRuleCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EmailImportImportRule> specification = createSpecification(criteria);
        return emailImportImportRuleRepository.findAll(specification, page).map(emailImportImportRuleMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EmailImportImportRuleCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<EmailImportImportRule> specification = createSpecification(criteria);
        return emailImportImportRuleRepository.count(specification);
    }

    /**
     * Function to convert {@link EmailImportImportRuleCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EmailImportImportRule> createSpecification(EmailImportImportRuleCriteria criteria) {
        Specification<EmailImportImportRule> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), EmailImportImportRule_.id),
                buildStringSpecification(criteria.getName(), EmailImportImportRule_.name),
                buildRangeSpecification(criteria.getPriority(), EmailImportImportRule_.priority),
                buildSpecification(criteria.getIsActive(), EmailImportImportRule_.isActive),
                buildRangeSpecification(criteria.getMatchCount(), EmailImportImportRule_.matchCount),
                buildRangeSpecification(criteria.getLastMatchDate(), EmailImportImportRule_.lastMatchDate),
                buildStringSpecification(criteria.getCreatedBy(), EmailImportImportRule_.createdBy),
                buildRangeSpecification(criteria.getCreatedDate(), EmailImportImportRule_.createdDate),
                buildRangeSpecification(criteria.getLastModifiedDate(), EmailImportImportRule_.lastModifiedDate),
                buildSpecification(criteria.getEmailImportImportMappingsId(), root ->
                    root.join(EmailImportImportRule_.emailImportImportMappings, JoinType.LEFT).get(EmailImportImportMapping_.id)
                ),
                buildSpecification(criteria.getEmailImportDocumentsId(), root ->
                    root.join(EmailImportImportRule_.emailImportDocuments, JoinType.LEFT).get(EmailImportDocument_.id)
                )
            );
        }
        return specification;
    }
}
