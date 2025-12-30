package fr.smartprod.paperdms.ocr.service;

import fr.smartprod.paperdms.ocr.domain.OcrJob;
import fr.smartprod.paperdms.ocr.repository.OcrJobRepository;
import fr.smartprod.paperdms.ocr.repository.search.OcrJobSearchRepository;
import fr.smartprod.paperdms.ocr.service.dto.OcrJobDTO;
import fr.smartprod.paperdms.ocr.service.mapper.OcrJobMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.ocr.domain.OcrJob}.
 */
@Service
@Transactional
public class OcrJobService {

    private static final Logger LOG = LoggerFactory.getLogger(OcrJobService.class);

    private final OcrJobRepository ocrJobRepository;

    private final OcrJobMapper ocrJobMapper;

    private final OcrJobSearchRepository ocrJobSearchRepository;

    public OcrJobService(OcrJobRepository ocrJobRepository, OcrJobMapper ocrJobMapper, OcrJobSearchRepository ocrJobSearchRepository) {
        this.ocrJobRepository = ocrJobRepository;
        this.ocrJobMapper = ocrJobMapper;
        this.ocrJobSearchRepository = ocrJobSearchRepository;
    }

    /**
     * Save a ocrJob.
     *
     * @param ocrJobDTO the entity to save.
     * @return the persisted entity.
     */
    public OcrJobDTO save(OcrJobDTO ocrJobDTO) {
        LOG.debug("Request to save OcrJob : {}", ocrJobDTO);
        OcrJob ocrJob = ocrJobMapper.toEntity(ocrJobDTO);
        ocrJob = ocrJobRepository.save(ocrJob);
        ocrJobSearchRepository.index(ocrJob);
        return ocrJobMapper.toDto(ocrJob);
    }

    /**
     * Update a ocrJob.
     *
     * @param ocrJobDTO the entity to save.
     * @return the persisted entity.
     */
    public OcrJobDTO update(OcrJobDTO ocrJobDTO) {
        LOG.debug("Request to update OcrJob : {}", ocrJobDTO);
        OcrJob ocrJob = ocrJobMapper.toEntity(ocrJobDTO);
        ocrJob = ocrJobRepository.save(ocrJob);
        ocrJobSearchRepository.index(ocrJob);
        return ocrJobMapper.toDto(ocrJob);
    }

    /**
     * Partially update a ocrJob.
     *
     * @param ocrJobDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OcrJobDTO> partialUpdate(OcrJobDTO ocrJobDTO) {
        LOG.debug("Request to partially update OcrJob : {}", ocrJobDTO);

        return ocrJobRepository
            .findById(ocrJobDTO.getId())
            .map(existingOcrJob -> {
                ocrJobMapper.partialUpdate(existingOcrJob, ocrJobDTO);

                return existingOcrJob;
            })
            .map(ocrJobRepository::save)
            .map(savedOcrJob -> {
                ocrJobSearchRepository.index(savedOcrJob);
                return savedOcrJob;
            })
            .map(ocrJobMapper::toDto);
    }

    /**
     * Get one ocrJob by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OcrJobDTO> findOne(Long id) {
        LOG.debug("Request to get OcrJob : {}", id);
        return ocrJobRepository.findById(id).map(ocrJobMapper::toDto);
    }

    /**
     * Delete the ocrJob by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete OcrJob : {}", id);
        ocrJobRepository.deleteById(id);
        ocrJobSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the ocrJob corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<OcrJobDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of OcrJobs for query {}", query);
        return ocrJobSearchRepository.search(query, pageable).map(ocrJobMapper::toDto);
    }
}
