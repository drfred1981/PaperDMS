package fr.smartprod.paperdms.gateway.service;

import fr.smartprod.paperdms.gateway.domain.*; // for static metamodels
import fr.smartprod.paperdms.gateway.domain.DocumentProcess;
import fr.smartprod.paperdms.gateway.repository.DocumentProcessRepository;
import fr.smartprod.paperdms.gateway.repository.search.DocumentProcessSearchRepository;
import fr.smartprod.paperdms.gateway.service.criteria.DocumentProcessCriteria;
import fr.smartprod.paperdms.gateway.service.dto.DocumentProcessDTO;
import fr.smartprod.paperdms.gateway.service.mapper.DocumentProcessMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link DocumentProcess} entities in the database.
 * The main input is a {@link DocumentProcessCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link DocumentProcessDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DocumentProcessQueryService extends QueryService<DocumentProcess> {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentProcessQueryService.class);

    private final DocumentProcessRepository documentProcessRepository;

    private final DocumentProcessMapper documentProcessMapper;

    private final DocumentProcessSearchRepository documentProcessSearchRepository;

    public DocumentProcessQueryService(
        DocumentProcessRepository documentProcessRepository,
        DocumentProcessMapper documentProcessMapper,
        DocumentProcessSearchRepository documentProcessSearchRepository
    ) {
        this.documentProcessRepository = documentProcessRepository;
        this.documentProcessMapper = documentProcessMapper;
        this.documentProcessSearchRepository = documentProcessSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link DocumentProcessDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DocumentProcessDTO> findByCriteria(DocumentProcessCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DocumentProcess> specification = createSpecification(criteria);
        return documentProcessRepository.findAll(specification, page).map(documentProcessMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DocumentProcessCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<DocumentProcess> specification = createSpecification(criteria);
        return documentProcessRepository.count(specification);
    }

    /**
     * Function to convert {@link DocumentProcessCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DocumentProcess> createSpecification(DocumentProcessCriteria criteria) {
        Specification<DocumentProcess> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), DocumentProcess_.id),
                buildSpecification(criteria.getStatus(), DocumentProcess_.status),
                buildStringSpecification(criteria.getDocumentSha256(), DocumentProcess_.documentSha256)
            );
        }
        return specification;
    }
}
