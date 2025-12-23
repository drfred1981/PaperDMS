package fr.smartprod.paperdms.ocr.service.impl;

import fr.smartprod.paperdms.ocr.domain.OcrCache;
import fr.smartprod.paperdms.ocr.repository.OcrCacheRepository;
import fr.smartprod.paperdms.ocr.service.OcrCacheService;
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
public class OcrCacheServiceImpl implements OcrCacheService {

    private static final Logger LOG = LoggerFactory.getLogger(OcrCacheServiceImpl.class);

    private final OcrCacheRepository ocrCacheRepository;

    private final OcrCacheMapper ocrCacheMapper;

    public OcrCacheServiceImpl(OcrCacheRepository ocrCacheRepository, OcrCacheMapper ocrCacheMapper) {
        this.ocrCacheRepository = ocrCacheRepository;
        this.ocrCacheMapper = ocrCacheMapper;
    }

    @Override
    public OcrCacheDTO save(OcrCacheDTO ocrCacheDTO) {
        LOG.debug("Request to save OcrCache : {}", ocrCacheDTO);
        OcrCache ocrCache = ocrCacheMapper.toEntity(ocrCacheDTO);
        ocrCache = ocrCacheRepository.save(ocrCache);
        return ocrCacheMapper.toDto(ocrCache);
    }

    @Override
    public OcrCacheDTO update(OcrCacheDTO ocrCacheDTO) {
        LOG.debug("Request to update OcrCache : {}", ocrCacheDTO);
        OcrCache ocrCache = ocrCacheMapper.toEntity(ocrCacheDTO);
        ocrCache = ocrCacheRepository.save(ocrCache);
        return ocrCacheMapper.toDto(ocrCache);
    }

    @Override
    public Optional<OcrCacheDTO> partialUpdate(OcrCacheDTO ocrCacheDTO) {
        LOG.debug("Request to partially update OcrCache : {}", ocrCacheDTO);

        return ocrCacheRepository
            .findById(ocrCacheDTO.getId())
            .map(existingOcrCache -> {
                ocrCacheMapper.partialUpdate(existingOcrCache, ocrCacheDTO);

                return existingOcrCache;
            })
            .map(ocrCacheRepository::save)
            .map(ocrCacheMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OcrCacheDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all OcrCaches");
        return ocrCacheRepository.findAll(pageable).map(ocrCacheMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OcrCacheDTO> findOne(Long id) {
        LOG.debug("Request to get OcrCache : {}", id);
        return ocrCacheRepository.findById(id).map(ocrCacheMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete OcrCache : {}", id);
        ocrCacheRepository.deleteById(id);
    }
}
