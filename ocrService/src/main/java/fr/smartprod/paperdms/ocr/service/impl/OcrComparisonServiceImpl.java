package fr.smartprod.paperdms.ocr.service.impl;

import fr.smartprod.paperdms.ocr.domain.OcrComparison;
import fr.smartprod.paperdms.ocr.repository.OcrComparisonRepository;
import fr.smartprod.paperdms.ocr.service.OcrComparisonService;
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
public class OcrComparisonServiceImpl implements OcrComparisonService {

    private static final Logger LOG = LoggerFactory.getLogger(OcrComparisonServiceImpl.class);

    private final OcrComparisonRepository ocrComparisonRepository;

    private final OcrComparisonMapper ocrComparisonMapper;

    public OcrComparisonServiceImpl(OcrComparisonRepository ocrComparisonRepository, OcrComparisonMapper ocrComparisonMapper) {
        this.ocrComparisonRepository = ocrComparisonRepository;
        this.ocrComparisonMapper = ocrComparisonMapper;
    }

    @Override
    public OcrComparisonDTO save(OcrComparisonDTO ocrComparisonDTO) {
        LOG.debug("Request to save OcrComparison : {}", ocrComparisonDTO);
        OcrComparison ocrComparison = ocrComparisonMapper.toEntity(ocrComparisonDTO);
        ocrComparison = ocrComparisonRepository.save(ocrComparison);
        return ocrComparisonMapper.toDto(ocrComparison);
    }

    @Override
    public OcrComparisonDTO update(OcrComparisonDTO ocrComparisonDTO) {
        LOG.debug("Request to update OcrComparison : {}", ocrComparisonDTO);
        OcrComparison ocrComparison = ocrComparisonMapper.toEntity(ocrComparisonDTO);
        ocrComparison = ocrComparisonRepository.save(ocrComparison);
        return ocrComparisonMapper.toDto(ocrComparison);
    }

    @Override
    public Optional<OcrComparisonDTO> partialUpdate(OcrComparisonDTO ocrComparisonDTO) {
        LOG.debug("Request to partially update OcrComparison : {}", ocrComparisonDTO);

        return ocrComparisonRepository
            .findById(ocrComparisonDTO.getId())
            .map(existingOcrComparison -> {
                ocrComparisonMapper.partialUpdate(existingOcrComparison, ocrComparisonDTO);

                return existingOcrComparison;
            })
            .map(ocrComparisonRepository::save)
            .map(ocrComparisonMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OcrComparisonDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all OcrComparisons");
        return ocrComparisonRepository.findAll(pageable).map(ocrComparisonMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OcrComparisonDTO> findOne(Long id) {
        LOG.debug("Request to get OcrComparison : {}", id);
        return ocrComparisonRepository.findById(id).map(ocrComparisonMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete OcrComparison : {}", id);
        ocrComparisonRepository.deleteById(id);
    }
}
