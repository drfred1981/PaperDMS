package fr.smartprod.paperdms.document.service;

import fr.smartprod.paperdms.document.domain.*; // for static metamodels
import fr.smartprod.paperdms.document.domain.DocumentServiceStatus;
import fr.smartprod.paperdms.document.repository.DocumentServiceStatusRepository;
import fr.smartprod.paperdms.document.repository.search.DocumentServiceStatusSearchRepository;
import fr.smartprod.paperdms.document.service.criteria.DocumentServiceStatusCriteria;
import fr.smartprod.paperdms.document.service.dto.DocumentServiceStatusDTO;
import fr.smartprod.paperdms.document.service.mapper.DocumentServiceStatusMapper;
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
 * Service for executing complex queries for {@link DocumentServiceStatus} entities in the database.
 * The main input is a {@link DocumentServiceStatusCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link DocumentServiceStatusDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DocumentServiceStatusQueryService extends QueryService<DocumentServiceStatus> {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentServiceStatusQueryService.class);

    private final DocumentServiceStatusRepository documentServiceStatusRepository;

    private final DocumentServiceStatusMapper documentServiceStatusMapper;

    private final DocumentServiceStatusSearchRepository documentServiceStatusSearchRepository;

    public DocumentServiceStatusQueryService(
        DocumentServiceStatusRepository documentServiceStatusRepository,
        DocumentServiceStatusMapper documentServiceStatusMapper,
        DocumentServiceStatusSearchRepository documentServiceStatusSearchRepository
    ) {
        this.documentServiceStatusRepository = documentServiceStatusRepository;
        this.documentServiceStatusMapper = documentServiceStatusMapper;
        this.documentServiceStatusSearchRepository = documentServiceStatusSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link DocumentServiceStatusDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DocumentServiceStatusDTO> findByCriteria(DocumentServiceStatusCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DocumentServiceStatus> specification = createSpecification(criteria);
        return documentServiceStatusRepository.findAll(specification, page).map(documentServiceStatusMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DocumentServiceStatusCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<DocumentServiceStatus> specification = createSpecification(criteria);
        return documentServiceStatusRepository.count(specification);
    }

    /**
     * Function to convert {@link DocumentServiceStatusCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DocumentServiceStatus> createSpecification(DocumentServiceStatusCriteria criteria) {
        Specification<DocumentServiceStatus> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), DocumentServiceStatus_.id),
                buildRangeSpecification(criteria.getDocumentId(), DocumentServiceStatus_.documentId),
                buildSpecification(criteria.getServiceType(), DocumentServiceStatus_.serviceType),
                buildSpecification(criteria.getStatus(), DocumentServiceStatus_.status),
                buildRangeSpecification(criteria.getRetryCount(), DocumentServiceStatus_.retryCount),
                buildRangeSpecification(criteria.getLastProcessedDate(), DocumentServiceStatus_.lastProcessedDate),
                buildRangeSpecification(criteria.getProcessingStartDate(), DocumentServiceStatus_.processingStartDate),
                buildRangeSpecification(criteria.getProcessingEndDate(), DocumentServiceStatus_.processingEndDate),
                buildRangeSpecification(criteria.getProcessingDuration(), DocumentServiceStatus_.processingDuration),
                buildStringSpecification(criteria.getJobId(), DocumentServiceStatus_.jobId),
                buildRangeSpecification(criteria.getPriority(), DocumentServiceStatus_.priority),
                buildStringSpecification(criteria.getUpdatedBy(), DocumentServiceStatus_.updatedBy),
                buildRangeSpecification(criteria.getUpdatedDate(), DocumentServiceStatus_.updatedDate),
                buildSpecification(criteria.getDocumentId(), root ->
                    root.join(DocumentServiceStatus_.document, JoinType.LEFT).get(Document_.id)
                )
            );
        }
        return specification;
    }
}
