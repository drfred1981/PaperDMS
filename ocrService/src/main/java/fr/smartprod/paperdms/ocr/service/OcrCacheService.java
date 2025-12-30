package fr.smartprod.paperdms.ocr.service;

import fr.smartprod.paperdms.ocr.domain.OcrCache;
import fr.smartprod.paperdms.ocr.repository.OcrCacheRepository;
import fr.smartprod.paperdms.ocr.repository.search.OcrCacheSearchRepository;
import fr.smartprod.paperdms.ocr.service.dto.OcrCacheDTO;
import fr.smartprod.paperdms.ocr.service.mapper.OcrCacheMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.ocr.domain.OcrCache}.
 */
@Service
@Transactional
public class OcrCacheService {

    private static final Logger LOG = LoggerFactory.getLogger(OcrCacheService.class);

    private final OcrCacheRepository ocrCacheRepository;

    private final OcrCacheMapper ocrCacheMapper;

    private final OcrCacheSearchRepository ocrCacheSearchRepository;

    public OcrCacheService(
        OcrCacheRepository ocrCacheRepository,
        OcrCacheMapper ocrCacheMapper,
        OcrCacheSearchRepository ocrCacheSearchRepository
    ) {
        this.ocrCacheRepository = ocrCacheRepository;
        this.ocrCacheMapper = ocrCacheMapper;
        this.ocrCacheSearchRepository = ocrCacheSearchRepository;
    }

    /**
     * Save a ocrCache.
     *
     * @param ocrCacheDTO the entity to save.
     * @return the persisted entity.
     */
    public OcrCacheDTO save(OcrCacheDTO ocrCacheDTO) {
        LOG.debug("Request to save OcrCache : {}", ocrCacheDTO);
        OcrCache ocrCache = ocrCacheMapper.toEntity(ocrCacheDTO);
        ocrCache = ocrCacheRepository.save(ocrCache);
        ocrCacheSearchRepository.index(ocrCache);
        return ocrCacheMapper.toDto(ocrCache);
    }

    /**
     * Update a ocrCache.
     *
     * @param ocrCacheDTO the entity to save.
     * @return the persisted entity.
     */
    public OcrCacheDTO update(OcrCacheDTO ocrCacheDTO) {
        LOG.debug("Request to update OcrCache : {}", ocrCacheDTO);
        OcrCache ocrCache = ocrCacheMapper.toEntity(ocrCacheDTO);
        ocrCache = ocrCacheRepository.save(ocrCache);
        ocrCacheSearchRepository.index(ocrCache);
        return ocrCacheMapper.toDto(ocrCache);
    }

    /**
     * Partially update a ocrCache.
     *
     * @param ocrCacheDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OcrCacheDTO> partialUpdate(OcrCacheDTO ocrCacheDTO) {
        LOG.debug("Request to partially update OcrCache : {}", ocrCacheDTO);

        return ocrCacheRepository
            .findById(ocrCacheDTO.getId())
            .map(existingOcrCache -> {
                ocrCacheMapper.partialUpdate(existingOcrCache, ocrCacheDTO);

                return existingOcrCache;
            })
            .map(ocrCacheRepository::save)
            .map(savedOcrCache -> {
                ocrCacheSearchRepository.index(savedOcrCache);
                return savedOcrCache;
            })
            .map(ocrCacheMapper::toDto);
    }

    /**
     * Get one ocrCache by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OcrCacheDTO> findOne(Long id) {
        LOG.debug("Request to get OcrCache : {}", id);
        return ocrCacheRepository.findById(id).map(ocrCacheMapper::toDto);
    }

    /**
     * Delete the ocrCache by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete OcrCache : {}", id);
        ocrCacheRepository.deleteById(id);
        ocrCacheSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the ocrCache corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<OcrCacheDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of OcrCaches for query {}", query);
        return ocrCacheSearchRepository.search(query, pageable).map(ocrCacheMapper::toDto);
    }
}
