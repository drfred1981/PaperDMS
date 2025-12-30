package fr.smartprod.paperdms.document.service;

import fr.smartprod.paperdms.document.domain.*; // for static metamodels
import fr.smartprod.paperdms.document.domain.DocumentRelation;
import fr.smartprod.paperdms.document.repository.DocumentRelationRepository;
import fr.smartprod.paperdms.document.repository.search.DocumentRelationSearchRepository;
import fr.smartprod.paperdms.document.service.criteria.DocumentRelationCriteria;
import fr.smartprod.paperdms.document.service.dto.DocumentRelationDTO;
import fr.smartprod.paperdms.document.service.mapper.DocumentRelationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link DocumentRelation} entities in the database.
 * The main input is a {@link DocumentRelationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link DocumentRelationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DocumentRelationQueryService extends QueryService<DocumentRelation> {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentRelationQueryService.class);

    private final DocumentRelationRepository documentRelationRepository;

    private final DocumentRelationMapper documentRelationMapper;

    private final DocumentRelationSearchRepository documentRelationSearchRepository;

    public DocumentRelationQueryService(
        DocumentRelationRepository documentRelationRepository,
        DocumentRelationMapper documentRelationMapper,
        DocumentRelationSearchRepository documentRelationSearchRepository
    ) {
        this.documentRelationRepository = documentRelationRepository;
        this.documentRelationMapper = documentRelationMapper;
        this.documentRelationSearchRepository = documentRelationSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link DocumentRelationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DocumentRelationDTO> findByCriteria(DocumentRelationCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DocumentRelation> specification = createSpecification(criteria);
        return documentRelationRepository.findAll(specification, page).map(documentRelationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DocumentRelationCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<DocumentRelation> specification = createSpecification(criteria);
        return documentRelationRepository.count(specification);
    }

    /**
     * Function to convert {@link DocumentRelationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DocumentRelation> createSpecification(DocumentRelationCriteria criteria) {
        Specification<DocumentRelation> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), DocumentRelation_.id),
                buildRangeSpecification(criteria.getSourceDocumentId(), DocumentRelation_.sourceDocumentId),
                buildRangeSpecification(criteria.getTargetDocumentId(), DocumentRelation_.targetDocumentId),
                buildSpecification(criteria.getRelationType(), DocumentRelation_.relationType),
                buildStringSpecification(criteria.getCreatedBy(), DocumentRelation_.createdBy),
                buildRangeSpecification(criteria.getCreatedDate(), DocumentRelation_.createdDate)
            );
        }
        return specification;
    }
}
