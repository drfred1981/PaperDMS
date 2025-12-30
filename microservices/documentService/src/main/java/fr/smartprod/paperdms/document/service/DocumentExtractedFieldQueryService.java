package fr.smartprod.paperdms.document.service;

import fr.smartprod.paperdms.document.domain.*; // for static metamodels
import fr.smartprod.paperdms.document.domain.DocumentExtractedField;
import fr.smartprod.paperdms.document.repository.DocumentExtractedFieldRepository;
import fr.smartprod.paperdms.document.repository.search.DocumentExtractedFieldSearchRepository;
import fr.smartprod.paperdms.document.service.criteria.DocumentExtractedFieldCriteria;
import fr.smartprod.paperdms.document.service.dto.DocumentExtractedFieldDTO;
import fr.smartprod.paperdms.document.service.mapper.DocumentExtractedFieldMapper;
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
 * Service for executing complex queries for {@link DocumentExtractedField} entities in the database.
 * The main input is a {@link DocumentExtractedFieldCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link DocumentExtractedFieldDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DocumentExtractedFieldQueryService extends QueryService<DocumentExtractedField> {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentExtractedFieldQueryService.class);

    private final DocumentExtractedFieldRepository documentExtractedFieldRepository;

    private final DocumentExtractedFieldMapper documentExtractedFieldMapper;

    private final DocumentExtractedFieldSearchRepository documentExtractedFieldSearchRepository;

    public DocumentExtractedFieldQueryService(
        DocumentExtractedFieldRepository documentExtractedFieldRepository,
        DocumentExtractedFieldMapper documentExtractedFieldMapper,
        DocumentExtractedFieldSearchRepository documentExtractedFieldSearchRepository
    ) {
        this.documentExtractedFieldRepository = documentExtractedFieldRepository;
        this.documentExtractedFieldMapper = documentExtractedFieldMapper;
        this.documentExtractedFieldSearchRepository = documentExtractedFieldSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link DocumentExtractedFieldDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DocumentExtractedFieldDTO> findByCriteria(DocumentExtractedFieldCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DocumentExtractedField> specification = createSpecification(criteria);
        return documentExtractedFieldRepository.findAll(specification, page).map(documentExtractedFieldMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DocumentExtractedFieldCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<DocumentExtractedField> specification = createSpecification(criteria);
        return documentExtractedFieldRepository.count(specification);
    }

    /**
     * Function to convert {@link DocumentExtractedFieldCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DocumentExtractedField> createSpecification(DocumentExtractedFieldCriteria criteria) {
        Specification<DocumentExtractedField> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), DocumentExtractedField_.id),
                buildStringSpecification(criteria.getFieldKey(), DocumentExtractedField_.fieldKey),
                buildRangeSpecification(criteria.getConfidence(), DocumentExtractedField_.confidence),
                buildSpecification(criteria.getExtractionMethod(), DocumentExtractedField_.extractionMethod),
                buildSpecification(criteria.getIsVerified(), DocumentExtractedField_.isVerified),
                buildRangeSpecification(criteria.getExtractedDate(), DocumentExtractedField_.extractedDate),
                buildSpecification(criteria.getDocumentId(), root ->
                    root.join(DocumentExtractedField_.document, JoinType.LEFT).get(Document_.id)
                )
            );
        }
        return specification;
    }
}
