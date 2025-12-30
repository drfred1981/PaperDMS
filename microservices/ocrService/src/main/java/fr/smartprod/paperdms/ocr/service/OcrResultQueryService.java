package fr.smartprod.paperdms.ocr.service;

import fr.smartprod.paperdms.ocr.domain.*; // for static metamodels
import fr.smartprod.paperdms.ocr.domain.OcrResult;
import fr.smartprod.paperdms.ocr.repository.OcrResultRepository;
import fr.smartprod.paperdms.ocr.repository.search.OcrResultSearchRepository;
import fr.smartprod.paperdms.ocr.service.criteria.OcrResultCriteria;
import fr.smartprod.paperdms.ocr.service.dto.OcrResultDTO;
import fr.smartprod.paperdms.ocr.service.mapper.OcrResultMapper;
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
 * Service for executing complex queries for {@link OcrResult} entities in the database.
 * The main input is a {@link OcrResultCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link OcrResultDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OcrResultQueryService extends QueryService<OcrResult> {

    private static final Logger LOG = LoggerFactory.getLogger(OcrResultQueryService.class);

    private final OcrResultRepository ocrResultRepository;

    private final OcrResultMapper ocrResultMapper;

    private final OcrResultSearchRepository ocrResultSearchRepository;

    public OcrResultQueryService(
        OcrResultRepository ocrResultRepository,
        OcrResultMapper ocrResultMapper,
        OcrResultSearchRepository ocrResultSearchRepository
    ) {
        this.ocrResultRepository = ocrResultRepository;
        this.ocrResultMapper = ocrResultMapper;
        this.ocrResultSearchRepository = ocrResultSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link OcrResultDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OcrResultDTO> findByCriteria(OcrResultCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<OcrResult> specification = createSpecification(criteria);
        return ocrResultRepository.findAll(specification, page).map(ocrResultMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OcrResultCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<OcrResult> specification = createSpecification(criteria);
        return ocrResultRepository.count(specification);
    }

    /**
     * Function to convert {@link OcrResultCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<OcrResult> createSpecification(OcrResultCriteria criteria) {
        Specification<OcrResult> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), OcrResult_.id),
                buildRangeSpecification(criteria.getPageNumber(), OcrResult_.pageNumber),
                buildStringSpecification(criteria.getPageSha256(), OcrResult_.pageSha256),
                buildRangeSpecification(criteria.getConfidence(), OcrResult_.confidence),
                buildStringSpecification(criteria.gets3ResultKey(), OcrResult_.s3ResultKey),
                buildStringSpecification(criteria.gets3Bucket(), OcrResult_.s3Bucket),
                buildStringSpecification(criteria.gets3BoundingBoxKey(), OcrResult_.s3BoundingBoxKey),
                buildStringSpecification(criteria.getLanguage(), OcrResult_.language),
                buildRangeSpecification(criteria.getWordCount(), OcrResult_.wordCount),
                buildSpecification(criteria.getOcrEngine(), OcrResult_.ocrEngine),
                buildRangeSpecification(criteria.getProcessingTime(), OcrResult_.processingTime),
                buildStringSpecification(criteria.getRawResponseS3Key(), OcrResult_.rawResponseS3Key),
                buildRangeSpecification(criteria.getProcessedDate(), OcrResult_.processedDate),
                buildSpecification(criteria.getJobId(), root -> root.join(OcrResult_.job, JoinType.LEFT).get(OcrJob_.id))
            );
        }
        return specification;
    }
}
