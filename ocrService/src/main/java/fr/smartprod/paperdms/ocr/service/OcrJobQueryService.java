package fr.smartprod.paperdms.ocr.service;

import fr.smartprod.paperdms.ocr.domain.*; // for static metamodels
import fr.smartprod.paperdms.ocr.domain.OcrJob;
import fr.smartprod.paperdms.ocr.repository.OcrJobRepository;
import fr.smartprod.paperdms.ocr.service.criteria.OcrJobCriteria;
import fr.smartprod.paperdms.ocr.service.dto.OcrJobDTO;
import fr.smartprod.paperdms.ocr.service.mapper.OcrJobMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link OcrJob} entities in the database.
 * The main input is a {@link OcrJobCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link OcrJobDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OcrJobQueryService extends QueryService<OcrJob> {

    private static final Logger LOG = LoggerFactory.getLogger(OcrJobQueryService.class);

    private final OcrJobRepository ocrJobRepository;

    private final OcrJobMapper ocrJobMapper;

    public OcrJobQueryService(OcrJobRepository ocrJobRepository, OcrJobMapper ocrJobMapper) {
        this.ocrJobRepository = ocrJobRepository;
        this.ocrJobMapper = ocrJobMapper;
    }

    /**
     * Return a {@link Page} of {@link OcrJobDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OcrJobDTO> findByCriteria(OcrJobCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<OcrJob> specification = createSpecification(criteria);
        return ocrJobRepository.findAll(specification, page).map(ocrJobMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OcrJobCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<OcrJob> specification = createSpecification(criteria);
        return ocrJobRepository.count(specification);
    }

    /**
     * Function to convert {@link OcrJobCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<OcrJob> createSpecification(OcrJobCriteria criteria) {
        Specification<OcrJob> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), OcrJob_.id),
                buildSpecification(criteria.getStatus(), OcrJob_.status),
                buildRangeSpecification(criteria.getDocumentId(), OcrJob_.documentId),
                buildStringSpecification(criteria.getDocumentSha256(), OcrJob_.documentSha256),
                buildStringSpecification(criteria.gets3Key(), OcrJob_.s3Key),
                buildStringSpecification(criteria.gets3Bucket(), OcrJob_.s3Bucket),
                buildStringSpecification(criteria.getRequestedLanguage(), OcrJob_.requestedLanguage),
                buildStringSpecification(criteria.getDetectedLanguage(), OcrJob_.detectedLanguage),
                buildRangeSpecification(criteria.getLanguageConfidence(), OcrJob_.languageConfidence),
                buildSpecification(criteria.getOcrEngine(), OcrJob_.ocrEngine),
                buildStringSpecification(criteria.getTikaEndpoint(), OcrJob_.tikaEndpoint),
                buildStringSpecification(criteria.getAiProvider(), OcrJob_.aiProvider),
                buildStringSpecification(criteria.getAiModel(), OcrJob_.aiModel),
                buildStringSpecification(criteria.getResultCacheKey(), OcrJob_.resultCacheKey),
                buildSpecification(criteria.getIsCached(), OcrJob_.isCached),
                buildRangeSpecification(criteria.getStartDate(), OcrJob_.startDate),
                buildRangeSpecification(criteria.getEndDate(), OcrJob_.endDate),
                buildRangeSpecification(criteria.getPageCount(), OcrJob_.pageCount),
                buildRangeSpecification(criteria.getProgress(), OcrJob_.progress),
                buildRangeSpecification(criteria.getRetryCount(), OcrJob_.retryCount),
                buildRangeSpecification(criteria.getPriority(), OcrJob_.priority),
                buildRangeSpecification(criteria.getProcessingTime(), OcrJob_.processingTime),
                buildRangeSpecification(criteria.getCostEstimate(), OcrJob_.costEstimate),
                buildRangeSpecification(criteria.getCreatedDate(), OcrJob_.createdDate),
                buildStringSpecification(criteria.getCreatedBy(), OcrJob_.createdBy)
            );
        }
        return specification;
    }
}
