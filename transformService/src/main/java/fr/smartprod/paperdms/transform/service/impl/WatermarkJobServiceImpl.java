package fr.smartprod.paperdms.transform.service.impl;

import fr.smartprod.paperdms.transform.domain.WatermarkJob;
import fr.smartprod.paperdms.transform.repository.WatermarkJobRepository;
import fr.smartprod.paperdms.transform.service.WatermarkJobService;
import fr.smartprod.paperdms.transform.service.dto.WatermarkJobDTO;
import fr.smartprod.paperdms.transform.service.mapper.WatermarkJobMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.transform.domain.WatermarkJob}.
 */
@Service
@Transactional
public class WatermarkJobServiceImpl implements WatermarkJobService {

    private static final Logger LOG = LoggerFactory.getLogger(WatermarkJobServiceImpl.class);

    private final WatermarkJobRepository watermarkJobRepository;

    private final WatermarkJobMapper watermarkJobMapper;

    public WatermarkJobServiceImpl(WatermarkJobRepository watermarkJobRepository, WatermarkJobMapper watermarkJobMapper) {
        this.watermarkJobRepository = watermarkJobRepository;
        this.watermarkJobMapper = watermarkJobMapper;
    }

    @Override
    public WatermarkJobDTO save(WatermarkJobDTO watermarkJobDTO) {
        LOG.debug("Request to save WatermarkJob : {}", watermarkJobDTO);
        WatermarkJob watermarkJob = watermarkJobMapper.toEntity(watermarkJobDTO);
        watermarkJob = watermarkJobRepository.save(watermarkJob);
        return watermarkJobMapper.toDto(watermarkJob);
    }

    @Override
    public WatermarkJobDTO update(WatermarkJobDTO watermarkJobDTO) {
        LOG.debug("Request to update WatermarkJob : {}", watermarkJobDTO);
        WatermarkJob watermarkJob = watermarkJobMapper.toEntity(watermarkJobDTO);
        watermarkJob = watermarkJobRepository.save(watermarkJob);
        return watermarkJobMapper.toDto(watermarkJob);
    }

    @Override
    public Optional<WatermarkJobDTO> partialUpdate(WatermarkJobDTO watermarkJobDTO) {
        LOG.debug("Request to partially update WatermarkJob : {}", watermarkJobDTO);

        return watermarkJobRepository
            .findById(watermarkJobDTO.getId())
            .map(existingWatermarkJob -> {
                watermarkJobMapper.partialUpdate(existingWatermarkJob, watermarkJobDTO);

                return existingWatermarkJob;
            })
            .map(watermarkJobRepository::save)
            .map(watermarkJobMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<WatermarkJobDTO> findOne(Long id) {
        LOG.debug("Request to get WatermarkJob : {}", id);
        return watermarkJobRepository.findById(id).map(watermarkJobMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete WatermarkJob : {}", id);
        watermarkJobRepository.deleteById(id);
    }
}
