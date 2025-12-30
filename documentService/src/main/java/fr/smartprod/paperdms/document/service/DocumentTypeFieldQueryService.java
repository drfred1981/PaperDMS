package fr.smartprod.paperdms.document.service;

import fr.smartprod.paperdms.document.domain.*; // for static metamodels
import fr.smartprod.paperdms.document.domain.DocumentTypeField;
import fr.smartprod.paperdms.document.repository.DocumentTypeFieldRepository;
import fr.smartprod.paperdms.document.repository.search.DocumentTypeFieldSearchRepository;
import fr.smartprod.paperdms.document.service.criteria.DocumentTypeFieldCriteria;
import fr.smartprod.paperdms.document.service.dto.DocumentTypeFieldDTO;
import fr.smartprod.paperdms.document.service.mapper.DocumentTypeFieldMapper;
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
 * Service for executing complex queries for {@link DocumentTypeField} entities in the database.
 * The main input is a {@link DocumentTypeFieldCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link DocumentTypeFieldDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DocumentTypeFieldQueryService extends QueryService<DocumentTypeField> {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentTypeFieldQueryService.class);

    private final DocumentTypeFieldRepository documentTypeFieldRepository;

    private final DocumentTypeFieldMapper documentTypeFieldMapper;

    private final DocumentTypeFieldSearchRepository documentTypeFieldSearchRepository;

    public DocumentTypeFieldQueryService(
        DocumentTypeFieldRepository documentTypeFieldRepository,
        DocumentTypeFieldMapper documentTypeFieldMapper,
        DocumentTypeFieldSearchRepository documentTypeFieldSearchRepository
    ) {
        this.documentTypeFieldRepository = documentTypeFieldRepository;
        this.documentTypeFieldMapper = documentTypeFieldMapper;
        this.documentTypeFieldSearchRepository = documentTypeFieldSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link DocumentTypeFieldDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DocumentTypeFieldDTO> findByCriteria(DocumentTypeFieldCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DocumentTypeField> specification = createSpecification(criteria);
        return documentTypeFieldRepository.findAll(specification, page).map(documentTypeFieldMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DocumentTypeFieldCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<DocumentTypeField> specification = createSpecification(criteria);
        return documentTypeFieldRepository.count(specification);
    }

    /**
     * Function to convert {@link DocumentTypeFieldCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DocumentTypeField> createSpecification(DocumentTypeFieldCriteria criteria) {
        Specification<DocumentTypeField> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), DocumentTypeField_.id),
                buildStringSpecification(criteria.getFieldKey(), DocumentTypeField_.fieldKey),
                buildStringSpecification(criteria.getFieldLabel(), DocumentTypeField_.fieldLabel),
                buildSpecification(criteria.getDataType(), DocumentTypeField_.dataType),
                buildSpecification(criteria.getIsRequired(), DocumentTypeField_.isRequired),
                buildSpecification(criteria.getIsSearchable(), DocumentTypeField_.isSearchable),
                buildRangeSpecification(criteria.getCreatedDate(), DocumentTypeField_.createdDate),
                buildSpecification(criteria.getDocumentTypeId(), root ->
                    root.join(DocumentTypeField_.documentType, JoinType.LEFT).get(DocumentType_.id)
                )
            );
        }
        return specification;
    }
}
