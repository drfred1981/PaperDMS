package fr.smartprod.paperdms.ocr.service.impl;

import fr.smartprod.paperdms.ocr.domain.OcrJob;
import fr.smartprod.paperdms.ocr.repository.OcrJobRepository;
import fr.smartprod.paperdms.ocr.service.OcrJobService;
import fr.smartprod.paperdms.ocr.service.dto.OcrJobDTO;
import fr.smartprod.paperdms.ocr.service.mapper.OcrJobMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.ocr.domain.OcrJob}.
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
