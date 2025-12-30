package fr.smartprod.paperdms.ocr.service;

import fr.smartprod.paperdms.ocr.domain.OcrResult;
import fr.smartprod.paperdms.ocr.repository.OcrResultRepository;
import fr.smartprod.paperdms.ocr.repository.search.OcrResultSearchRepository;
import fr.smartprod.paperdms.ocr.service.dto.OcrResultDTO;
import fr.smartprod.paperdms.ocr.service.mapper.OcrResultMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.ocr.domain.OcrResult}.
 */
@Service
@Transactional
public class OcrResultService {

    private static final Logger LOG = LoggerFactory.getLogger(OcrResultService.class);

    private final OcrResultRepository ocrResultRepository;

    private final OcrResultMapper ocrResultMapper;

    private final OcrResultSearchRepository ocrResultSearchRepository;

    public OcrResultService(
        OcrResultRepository ocrResultRepository,
        OcrResultMapper ocrResultMapper,
        OcrResultSearchRepository ocrResultSearchRepository
    ) {
        this.ocrResultRepository = ocrResultRepository;
        this.ocrResultMapper = ocrResultMapper;
        this.ocrResultSearchRepository = ocrResultSearchRepository;
    }

    /**
     * Save a ocrResult.
     *
     * @param ocrResultDTO the entity to save.
     * @return the persisted entity.
     */
    public OcrResultDTO save(OcrResultDTO ocrResultDTO) {
        LOG.debug("Request to save OcrResult : {}", ocrResultDTO);
        OcrResult ocrResult = ocrResultMapper.toEntity(ocrResultDTO);
        ocrResult = ocrResultRepository.save(ocrResult);
        ocrResultSearchRepository.index(ocrResult);
        return ocrResultMapper.toDto(ocrResult);
    }

    /**
     * Update a ocrResult.
     *
     * @param ocrResultDTO the entity to save.
     * @return the persisted entity.
     */
    public OcrResultDTO update(OcrResultDTO ocrResultDTO) {
        LOG.debug("Request to update OcrResult : {}", ocrResultDTO);
        OcrResult ocrResult = ocrResultMapper.toEntity(ocrResultDTO);
        ocrResult = ocrResultRepository.save(ocrResult);
        ocrResultSearchRepository.index(ocrResult);
        return ocrResultMapper.toDto(ocrResult);
    }

    /**
     * Partially update a ocrResult.
     *
     * @param ocrResultDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OcrResultDTO> partialUpdate(OcrResultDTO ocrResultDTO) {
        LOG.debug("Request to partially update OcrResult : {}", ocrResultDTO);

        return ocrResultRepository
            .findById(ocrResultDTO.getId())
            .map(existingOcrResult -> {
                ocrResultMapper.partialUpdate(existingOcrResult, ocrResultDTO);

                return existingOcrResult;
            })
            .map(ocrResultRepository::save)
            .map(savedOcrResult -> {
                ocrResultSearchRepository.index(savedOcrResult);
                return savedOcrResult;
            })
            .map(ocrResultMapper::toDto);
    }

    /**
     * Get one ocrResult by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OcrResultDTO> findOne(Long id) {
        LOG.debug("Request to get OcrResult : {}", id);
        return ocrResultRepository.findById(id).map(ocrResultMapper::toDto);
    }

    /**
     * Delete the ocrResult by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete OcrResult : {}", id);
        ocrResultRepository.deleteById(id);
        ocrResultSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the ocrResult corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<OcrResultDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of OcrResults for query {}", query);
        return ocrResultSearchRepository.search(query, pageable).map(ocrResultMapper::toDto);
    }
}
