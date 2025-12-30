package fr.smartprod.paperdms.document.service;

import fr.smartprod.paperdms.document.domain.*; // for static metamodels
import fr.smartprod.paperdms.document.domain.DocumentTag;
import fr.smartprod.paperdms.document.repository.DocumentTagRepository;
import fr.smartprod.paperdms.document.repository.search.DocumentTagSearchRepository;
import fr.smartprod.paperdms.document.service.criteria.DocumentTagCriteria;
import fr.smartprod.paperdms.document.service.dto.DocumentTagDTO;
import fr.smartprod.paperdms.document.service.mapper.DocumentTagMapper;
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
 * Service for executing complex queries for {@link DocumentTag} entities in the database.
 * The main input is a {@link DocumentTagCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link DocumentTagDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DocumentTagQueryService extends QueryService<DocumentTag> {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentTagQueryService.class);

    private final DocumentTagRepository documentTagRepository;

    private final DocumentTagMapper documentTagMapper;

    private final DocumentTagSearchRepository documentTagSearchRepository;

    public DocumentTagQueryService(
        DocumentTagRepository documentTagRepository,
        DocumentTagMapper documentTagMapper,
        DocumentTagSearchRepository documentTagSearchRepository
    ) {
        this.documentTagRepository = documentTagRepository;
        this.documentTagMapper = documentTagMapper;
        this.documentTagSearchRepository = documentTagSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link DocumentTagDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DocumentTagDTO> findByCriteria(DocumentTagCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DocumentTag> specification = createSpecification(criteria);
        return documentTagRepository.findAll(specification, page).map(documentTagMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DocumentTagCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<DocumentTag> specification = createSpecification(criteria);
        return documentTagRepository.count(specification);
    }

    /**
     * Function to convert {@link DocumentTagCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DocumentTag> createSpecification(DocumentTagCriteria criteria) {
        Specification<DocumentTag> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), DocumentTag_.id),
                buildRangeSpecification(criteria.getAssignedDate(), DocumentTag_.assignedDate),
                buildStringSpecification(criteria.getAssignedBy(), DocumentTag_.assignedBy),
                buildRangeSpecification(criteria.getConfidence(), DocumentTag_.confidence),
                buildSpecification(criteria.getIsAutoMetaTagged(), DocumentTag_.isAutoMetaTagged),
                buildSpecification(criteria.getSource(), DocumentTag_.source),
                buildSpecification(criteria.getDocumentId(), root -> root.join(DocumentTag_.document, JoinType.LEFT).get(Document_.id)),
                buildSpecification(criteria.getMetaTagId(), root -> root.join(DocumentTag_.metaTag, JoinType.LEFT).get(MetaTag_.id))
            );
        }
        return specification;
    }
}
