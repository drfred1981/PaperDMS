package com.ged.ocr.service.impl;

import com.ged.ocr.domain.OcrJob;
import com.ged.ocr.repository.OcrJobRepository;
import com.ged.ocr.service.OcrJobService;
import com.ged.ocr.service.dto.OcrJobDTO;
import com.ged.ocr.service.mapper.OcrJobMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.ged.ocr.domain.OcrJob}.
 */
@Service
@Transactional
public class OcrJobServiceImpl implements OcrJobService {

    private static final Logger LOG = LoggerFactory.getLogger(OcrJobServiceImpl.class);

    private final OcrJobRepository ocrJobRepository;

    private final OcrJobMapper ocrJobMapper;

    public OcrJobServiceImpl(OcrJobRepository ocrJobRepository, OcrJobMapper ocrJobMapper) {
        this.ocrJobRepository = ocrJobRepository;
        this.ocrJobMapper = ocrJobMapper;
    }

    @Override
    public OcrJobDTO save(OcrJobDTO ocrJobDTO) {
        LOG.debug("Request to save OcrJob : {}", ocrJobDTO);
        OcrJob ocrJob = ocrJobMapper.toEntity(ocrJobDTO);
        ocrJob = ocrJobRepository.save(ocrJob);
        return ocrJobMapper.toDto(ocrJob);
    }

    @Override
    public OcrJobDTO update(OcrJobDTO ocrJobDTO) {
        LOG.debug("Request to update OcrJob : {}", ocrJobDTO);
        OcrJob ocrJob = ocrJobMapper.toEntity(ocrJobDTO);
        ocrJob = ocrJobRepository.save(ocrJob);
        return ocrJobMapper.toDto(ocrJob);
    }

    @Override
    public Optional<OcrJobDTO> partialUpdate(OcrJobDTO ocrJobDTO) {
        LOG.debug("Request to partially update OcrJob : {}", ocrJobDTO);

        return ocrJobRepository
            .findById(ocrJobDTO.getId())
            .map(existingOcrJob -> {
                ocrJobMapper.partialUpdate(existingOcrJob, ocrJobDTO);

                return existingOcrJob;
            })
            .map(ocrJobRepository::save)
            .map(ocrJobMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OcrJobDTO> findOne(Long id) {
        LOG.debug("Request to get OcrJob : {}", id);
        return ocrJobRepository.findById(id).map(ocrJobMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete OcrJob : {}", id);
        ocrJobRepository.deleteById(id);
    }
}
