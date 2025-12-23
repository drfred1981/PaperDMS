package fr.smartprod.paperdms.ocr.service.impl;

import fr.smartprod.paperdms.ocr.domain.OcrResult;
import fr.smartprod.paperdms.ocr.repository.OcrResultRepository;
import fr.smartprod.paperdms.ocr.service.OcrResultService;
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
public class OcrResultServiceImpl implements OcrResultService {

    private static final Logger LOG = LoggerFactory.getLogger(OcrResultServiceImpl.class);

    private final OcrResultRepository ocrResultRepository;

    private final OcrResultMapper ocrResultMapper;

    public OcrResultServiceImpl(OcrResultRepository ocrResultRepository, OcrResultMapper ocrResultMapper) {
        this.ocrResultRepository = ocrResultRepository;
        this.ocrResultMapper = ocrResultMapper;
    }

    @Override
    public OcrResultDTO save(OcrResultDTO ocrResultDTO) {
        LOG.debug("Request to save OcrResult : {}", ocrResultDTO);
        OcrResult ocrResult = ocrResultMapper.toEntity(ocrResultDTO);
        ocrResult = ocrResultRepository.save(ocrResult);
        return ocrResultMapper.toDto(ocrResult);
    }

    @Override
    public OcrResultDTO update(OcrResultDTO ocrResultDTO) {
        LOG.debug("Request to update OcrResult : {}", ocrResultDTO);
        OcrResult ocrResult = ocrResultMapper.toEntity(ocrResultDTO);
        ocrResult = ocrResultRepository.save(ocrResult);
        return ocrResultMapper.toDto(ocrResult);
    }

    @Override
    public Optional<OcrResultDTO> partialUpdate(OcrResultDTO ocrResultDTO) {
        LOG.debug("Request to partially update OcrResult : {}", ocrResultDTO);

        return ocrResultRepository
            .findById(ocrResultDTO.getId())
            .map(existingOcrResult -> {
                ocrResultMapper.partialUpdate(existingOcrResult, ocrResultDTO);

                return existingOcrResult;
            })
            .map(ocrResultRepository::save)
            .map(ocrResultMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OcrResultDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all OcrResults");
        return ocrResultRepository.findAll(pageable).map(ocrResultMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OcrResultDTO> findOne(Long id) {
        LOG.debug("Request to get OcrResult : {}", id);
        return ocrResultRepository.findById(id).map(ocrResultMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete OcrResult : {}", id);
        ocrResultRepository.deleteById(id);
    }
}
