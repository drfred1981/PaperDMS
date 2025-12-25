package fr.smartprod.paperdms.document.service;

import fr.smartprod.paperdms.document.domain.*; // for static metamodels
import fr.smartprod.paperdms.document.domain.DocumentType;
import fr.smartprod.paperdms.document.repository.DocumentTypeRepository;
import fr.smartprod.paperdms.document.repository.search.DocumentTypeSearchRepository;
import fr.smartprod.paperdms.document.service.criteria.DocumentTypeCriteria;
import fr.smartprod.paperdms.document.service.dto.DocumentTypeDTO;
import fr.smartprod.paperdms.document.service.mapper.DocumentTypeMapper;
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
 * Service for executing complex queries for {@link DocumentType} entities in the database.
 * The main input is a {@link DocumentTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link DocumentTypeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DocumentTypeQueryService extends QueryService<DocumentType> {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentTypeQueryService.class);

    private final DocumentTypeRepository documentTypeRepository;

    private final DocumentTypeMapper documentTypeMapper;

    private final DocumentTypeSearchRepository documentTypeSearchRepository;

    public DocumentTypeQueryService(
        DocumentTypeRepository documentTypeRepository,
        DocumentTypeMapper documentTypeMapper,
        DocumentTypeSearchRepository documentTypeSearchRepository
    ) {
        this.documentTypeRepository = documentTypeRepository;
        this.documentTypeMapper = documentTypeMapper;
        this.documentTypeSearchRepository = documentTypeSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link DocumentTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DocumentTypeDTO> findByCriteria(DocumentTypeCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DocumentType> specification = createSpecification(criteria);
        return documentTypeRepository.findAll(specification, page).map(documentTypeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DocumentTypeCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<DocumentType> specification = createSpecification(criteria);
        return documentTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link DocumentTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DocumentType> createSpecification(DocumentTypeCriteria criteria) {
        Specification<DocumentType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), DocumentType_.id),
                buildStringSpecification(criteria.getName(), DocumentType_.name),
                buildStringSpecification(criteria.getCode(), DocumentType_.code),
                buildStringSpecification(criteria.getIcon(), DocumentType_.icon),
                buildStringSpecification(criteria.getColor(), DocumentType_.color),
                buildSpecification(criteria.getIsActive(), DocumentType_.isActive),
                buildRangeSpecification(criteria.getCreatedDate(), DocumentType_.createdDate),
                buildStringSpecification(criteria.getCreatedBy(), DocumentType_.createdBy),
                buildSpecification(criteria.getFieldsId(), root -> root.join(DocumentType_.fields, JoinType.LEFT).get(DocumentTypeField_.id)
                )
            );
        }
        return specification;
    }
}
