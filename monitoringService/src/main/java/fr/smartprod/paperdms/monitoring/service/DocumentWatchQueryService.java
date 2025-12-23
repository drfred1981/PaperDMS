package fr.smartprod.paperdms.monitoring.service;

import fr.smartprod.paperdms.monitoring.domain.*; // for static metamodels
import fr.smartprod.paperdms.monitoring.domain.DocumentWatch;
import fr.smartprod.paperdms.monitoring.repository.DocumentWatchRepository;
import fr.smartprod.paperdms.monitoring.service.criteria.DocumentWatchCriteria;
import fr.smartprod.paperdms.monitoring.service.dto.DocumentWatchDTO;
import fr.smartprod.paperdms.monitoring.service.mapper.DocumentWatchMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link DocumentWatch} entities in the database.
 * The main input is a {@link DocumentWatchCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link DocumentWatchDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DocumentWatchQueryService extends QueryService<DocumentWatch> {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentWatchQueryService.class);

    private final DocumentWatchRepository documentWatchRepository;

    private final DocumentWatchMapper documentWatchMapper;

    public DocumentWatchQueryService(DocumentWatchRepository documentWatchRepository, DocumentWatchMapper documentWatchMapper) {
        this.documentWatchRepository = documentWatchRepository;
        this.documentWatchMapper = documentWatchMapper;
    }

    /**
     * Return a {@link Page} of {@link DocumentWatchDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DocumentWatchDTO> findByCriteria(DocumentWatchCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DocumentWatch> specification = createSpecification(criteria);
        return documentWatchRepository.findAll(specification, page).map(documentWatchMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DocumentWatchCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<DocumentWatch> specification = createSpecification(criteria);
        return documentWatchRepository.count(specification);
    }

    /**
     * Function to convert {@link DocumentWatchCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DocumentWatch> createSpecification(DocumentWatchCriteria criteria) {
        Specification<DocumentWatch> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), DocumentWatch_.id),
                buildRangeSpecification(criteria.getDocumentId(), DocumentWatch_.documentId),
                buildStringSpecification(criteria.getUserId(), DocumentWatch_.userId),
                buildSpecification(criteria.getWatchType(), DocumentWatch_.watchType),
                buildSpecification(criteria.getNotifyOnView(), DocumentWatch_.notifyOnView),
                buildSpecification(criteria.getNotifyOnDownload(), DocumentWatch_.notifyOnDownload),
                buildSpecification(criteria.getNotifyOnModify(), DocumentWatch_.notifyOnModify),
                buildSpecification(criteria.getNotifyOnShare(), DocumentWatch_.notifyOnShare),
                buildSpecification(criteria.getNotifyOnDelete(), DocumentWatch_.notifyOnDelete),
                buildRangeSpecification(criteria.getCreatedDate(), DocumentWatch_.createdDate)
            );
        }
        return specification;
    }
}
