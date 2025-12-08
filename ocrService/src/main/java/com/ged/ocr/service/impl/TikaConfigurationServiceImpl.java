package com.ged.ocr.service.impl;

import com.ged.ocr.domain.TikaConfiguration;
import com.ged.ocr.repository.TikaConfigurationRepository;
import com.ged.ocr.service.TikaConfigurationService;
import com.ged.ocr.service.dto.TikaConfigurationDTO;
import com.ged.ocr.service.mapper.TikaConfigurationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.ged.ocr.domain.TikaConfiguration}.
 */
@Service
@Transactional
public class TikaConfigurationServiceImpl implements TikaConfigurationService {

    private static final Logger LOG = LoggerFactory.getLogger(TikaConfigurationServiceImpl.class);

    private final TikaConfigurationRepository tikaConfigurationRepository;

    private final TikaConfigurationMapper tikaConfigurationMapper;

    public TikaConfigurationServiceImpl(
        TikaConfigurationRepository tikaConfigurationRepository,
        TikaConfigurationMapper tikaConfigurationMapper
    ) {
        this.tikaConfigurationRepository = tikaConfigurationRepository;
        this.tikaConfigurationMapper = tikaConfigurationMapper;
    }

    @Override
    public TikaConfigurationDTO save(TikaConfigurationDTO tikaConfigurationDTO) {
        LOG.debug("Request to save TikaConfiguration : {}", tikaConfigurationDTO);
        TikaConfiguration tikaConfiguration = tikaConfigurationMapper.toEntity(tikaConfigurationDTO);
        tikaConfiguration = tikaConfigurationRepository.save(tikaConfiguration);
        return tikaConfigurationMapper.toDto(tikaConfiguration);
    }

    @Override
    public TikaConfigurationDTO update(TikaConfigurationDTO tikaConfigurationDTO) {
        LOG.debug("Request to update TikaConfiguration : {}", tikaConfigurationDTO);
        TikaConfiguration tikaConfiguration = tikaConfigurationMapper.toEntity(tikaConfigurationDTO);
        tikaConfiguration = tikaConfigurationRepository.save(tikaConfiguration);
        return tikaConfigurationMapper.toDto(tikaConfiguration);
    }

    @Override
    public Optional<TikaConfigurationDTO> partialUpdate(TikaConfigurationDTO tikaConfigurationDTO) {
        LOG.debug("Request to partially update TikaConfiguration : {}", tikaConfigurationDTO);

        return tikaConfigurationRepository
            .findById(tikaConfigurationDTO.getId())
            .map(existingTikaConfiguration -> {
                tikaConfigurationMapper.partialUpdate(existingTikaConfiguration, tikaConfigurationDTO);

                return existingTikaConfiguration;
            })
            .map(tikaConfigurationRepository::save)
            .map(tikaConfigurationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TikaConfigurationDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all TikaConfigurations");
        return tikaConfigurationRepository.findAll(pageable).map(tikaConfigurationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TikaConfigurationDTO> findOne(Long id) {
        LOG.debug("Request to get TikaConfiguration : {}", id);
        return tikaConfigurationRepository.findById(id).map(tikaConfigurationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete TikaConfiguration : {}", id);
        tikaConfigurationRepository.deleteById(id);
    }
}
