package fr.smartprod.paperdms.document.service;

import fr.smartprod.paperdms.document.domain.*; // for static metamodels
import fr.smartprod.paperdms.document.domain.DocumentTemplate;
import fr.smartprod.paperdms.document.repository.DocumentTemplateRepository;
import fr.smartprod.paperdms.document.repository.search.DocumentTemplateSearchRepository;
import fr.smartprod.paperdms.document.service.criteria.DocumentTemplateCriteria;
import fr.smartprod.paperdms.document.service.dto.DocumentTemplateDTO;
import fr.smartprod.paperdms.document.service.mapper.DocumentTemplateMapper;
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
 * Service for executing complex queries for {@link DocumentTemplate} entities in the database.
 * The main input is a {@link DocumentTemplateCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link DocumentTemplateDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DocumentTemplateQueryService extends QueryService<DocumentTemplate> {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentTemplateQueryService.class);

    private final DocumentTemplateRepository documentTemplateRepository;

    private final DocumentTemplateMapper documentTemplateMapper;

    private final DocumentTemplateSearchRepository documentTemplateSearchRepository;

    public DocumentTemplateQueryService(
        DocumentTemplateRepository documentTemplateRepository,
        DocumentTemplateMapper documentTemplateMapper,
        DocumentTemplateSearchRepository documentTemplateSearchRepository
    ) {
        this.documentTemplateRepository = documentTemplateRepository;
        this.documentTemplateMapper = documentTemplateMapper;
        this.documentTemplateSearchRepository = documentTemplateSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link DocumentTemplateDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DocumentTemplateDTO> findByCriteria(DocumentTemplateCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DocumentTemplate> specification = createSpecification(criteria);
        return documentTemplateRepository.findAll(specification, page).map(documentTemplateMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DocumentTemplateCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<DocumentTemplate> specification = createSpecification(criteria);
        return documentTemplateRepository.count(specification);
    }

    /**
     * Function to convert {@link DocumentTemplateCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DocumentTemplate> createSpecification(DocumentTemplateCriteria criteria) {
        Specification<DocumentTemplate> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), DocumentTemplate_.id),
                buildStringSpecification(criteria.getName(), DocumentTemplate_.name),
                buildStringSpecification(criteria.getTemplateSha256(), DocumentTemplate_.templateSha256),
                buildStringSpecification(criteria.getTemplateS3Key(), DocumentTemplate_.templateS3Key),
                buildSpecification(criteria.getIsActive(), DocumentTemplate_.isActive),
                buildStringSpecification(criteria.getCreatedBy(), DocumentTemplate_.createdBy),
                buildRangeSpecification(criteria.getCreatedDate(), DocumentTemplate_.createdDate),
                buildSpecification(criteria.getDocumentTypeId(), root ->
                    root.join(DocumentTemplate_.documentType, JoinType.LEFT).get(DocumentType_.id)
                )
            );
        }
        return specification;
    }
}
