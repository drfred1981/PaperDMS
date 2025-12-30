package fr.smartprod.paperdms.ocr.service;

import fr.smartprod.paperdms.ocr.domain.OcrComparison;
import fr.smartprod.paperdms.ocr.repository.OcrComparisonRepository;
import fr.smartprod.paperdms.ocr.repository.search.OcrComparisonSearchRepository;
import fr.smartprod.paperdms.ocr.service.dto.OcrComparisonDTO;
import fr.smartprod.paperdms.ocr.service.mapper.OcrComparisonMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.ocr.domain.OcrComparison}.
 */
@Service
@Transactional
public class OcrComparisonService {

    private static final Logger LOG = LoggerFactory.getLogger(OcrComparisonService.class);

    private final OcrComparisonRepository ocrComparisonRepository;

    private final OcrComparisonMapper ocrComparisonMapper;

    private final OcrComparisonSearchRepository ocrComparisonSearchRepository;

    public OcrComparisonService(
        OcrComparisonRepository ocrComparisonRepository,
        OcrComparisonMapper ocrComparisonMapper,
        OcrComparisonSearchRepository ocrComparisonSearchRepository
    ) {
        this.ocrComparisonRepository = ocrComparisonRepository;
        this.ocrComparisonMapper = ocrComparisonMapper;
        this.ocrComparisonSearchRepository = ocrComparisonSearchRepository;
    }

    /**
     * Save a ocrComparison.
     *
     * @param ocrComparisonDTO the entity to save.
     * @return the persisted entity.
     */
    public OcrComparisonDTO save(OcrComparisonDTO ocrComparisonDTO) {
        LOG.debug("Request to save OcrComparison : {}", ocrComparisonDTO);
        OcrComparison ocrComparison = ocrComparisonMapper.toEntity(ocrComparisonDTO);
        ocrComparison = ocrComparisonRepository.save(ocrComparison);
        ocrComparisonSearchRepository.index(ocrComparison);
        return ocrComparisonMapper.toDto(ocrComparison);
    }

    /**
     * Update a ocrComparison.
     *
     * @param ocrComparisonDTO the entity to save.
     * @return the persisted entity.
     */
    public OcrComparisonDTO update(OcrComparisonDTO ocrComparisonDTO) {
        LOG.debug("Request to update OcrComparison : {}", ocrComparisonDTO);
        OcrComparison ocrComparison = ocrComparisonMapper.toEntity(ocrComparisonDTO);
        ocrComparison = ocrComparisonRepository.save(ocrComparison);
        ocrComparisonSearchRepository.index(ocrComparison);
        return ocrComparisonMapper.toDto(ocrComparison);
    }

    /**
     * Partially update a ocrComparison.
     *
     * @param ocrComparisonDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OcrComparisonDTO> partialUpdate(OcrComparisonDTO ocrComparisonDTO) {
        LOG.debug("Request to partially update OcrComparison : {}", ocrComparisonDTO);

        return ocrComparisonRepository
            .findById(ocrComparisonDTO.getId())
            .map(existingOcrComparison -> {
                ocrComparisonMapper.partialUpdate(existingOcrComparison, ocrComparisonDTO);

                return existingOcrComparison;
            })
            .map(ocrComparisonRepository::save)
            .map(savedOcrComparison -> {
                ocrComparisonSearchRepository.index(savedOcrComparison);
                return savedOcrComparison;
            })
            .map(ocrComparisonMapper::toDto);
    }

    /**
     * Get one ocrComparison by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OcrComparisonDTO> findOne(Long id) {
        LOG.debug("Request to get OcrComparison : {}", id);
        return ocrComparisonRepository.findById(id).map(ocrComparisonMapper::toDto);
    }

    /**
     * Delete the ocrComparison by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete OcrComparison : {}", id);
        ocrComparisonRepository.deleteById(id);
        ocrComparisonSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the ocrComparison corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<OcrComparisonDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of OcrComparisons for query {}", query);
        return ocrComparisonSearchRepository.search(query, pageable).map(ocrComparisonMapper::toDto);
    }
}
