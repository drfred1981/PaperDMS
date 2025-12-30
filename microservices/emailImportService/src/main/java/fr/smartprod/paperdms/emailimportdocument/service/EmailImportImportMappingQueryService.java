package fr.smartprod.paperdms.emailimportdocument.service;

import fr.smartprod.paperdms.emailimportdocument.domain.*; // for static metamodels
import fr.smartprod.paperdms.emailimportdocument.domain.EmailImportImportMapping;
import fr.smartprod.paperdms.emailimportdocument.repository.EmailImportImportMappingRepository;
import fr.smartprod.paperdms.emailimportdocument.service.criteria.EmailImportImportMappingCriteria;
import fr.smartprod.paperdms.emailimportdocument.service.dto.EmailImportImportMappingDTO;
import fr.smartprod.paperdms.emailimportdocument.service.mapper.EmailImportImportMappingMapper;
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
 * Service for executing complex queries for {@link EmailImportImportMapping} entities in the database.
 * The main input is a {@link EmailImportImportMappingCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link EmailImportImportMappingDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EmailImportImportMappingQueryService extends QueryService<EmailImportImportMapping> {

    private static final Logger LOG = LoggerFactory.getLogger(EmailImportImportMappingQueryService.class);

    private final EmailImportImportMappingRepository emailImportImportMappingRepository;

    private final EmailImportImportMappingMapper emailImportImportMappingMapper;

    public EmailImportImportMappingQueryService(
        EmailImportImportMappingRepository emailImportImportMappingRepository,
        EmailImportImportMappingMapper emailImportImportMappingMapper
    ) {
        this.emailImportImportMappingRepository = emailImportImportMappingRepository;
        this.emailImportImportMappingMapper = emailImportImportMappingMapper;
    }

    /**
     * Return a {@link Page} of {@link EmailImportImportMappingDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EmailImportImportMappingDTO> findByCriteria(EmailImportImportMappingCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EmailImportImportMapping> specification = createSpecification(criteria);
        return emailImportImportMappingRepository.findAll(specification, page).map(emailImportImportMappingMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EmailImportImportMappingCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<EmailImportImportMapping> specification = createSpecification(criteria);
        return emailImportImportMappingRepository.count(specification);
    }

    /**
     * Function to convert {@link EmailImportImportMappingCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EmailImportImportMapping> createSpecification(EmailImportImportMappingCriteria criteria) {
        Specification<EmailImportImportMapping> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), EmailImportImportMapping_.id),
                buildSpecification(criteria.getEmailField(), EmailImportImportMapping_.emailField),
                buildStringSpecification(criteria.getDocumentField(), EmailImportImportMapping_.documentField),
                buildSpecification(criteria.getTransformation(), EmailImportImportMapping_.transformation),
                buildSpecification(criteria.getIsRequired(), EmailImportImportMapping_.isRequired),
                buildStringSpecification(criteria.getDefaultValue(), EmailImportImportMapping_.defaultValue),
                buildStringSpecification(criteria.getValidationRegex(), EmailImportImportMapping_.validationRegex),
                buildSpecification(criteria.getRuleId(), root ->
                    root.join(EmailImportImportMapping_.rule, JoinType.LEFT).get(EmailImportImportRule_.id)
                )
            );
        }
        return specification;
    }
}
