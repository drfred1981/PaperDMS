package com.ged.ai.service.impl;

import com.ged.ai.domain.AutoTagJob;
import com.ged.ai.repository.AutoTagJobRepository;
import com.ged.ai.service.AutoTagJobService;
import com.ged.ai.service.dto.AutoTagJobDTO;
import com.ged.ai.service.mapper.AutoTagJobMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.ged.ai.domain.AutoTagJob}.
 */
@Service
@Transactional
public class AutoTagJobServiceImpl implements AutoTagJobService {

    private static final Logger LOG = LoggerFactory.getLogger(AutoTagJobServiceImpl.class);

    private final AutoTagJobRepository autoTagJobRepository;

    private final AutoTagJobMapper autoTagJobMapper;

    public AutoTagJobServiceImpl(AutoTagJobRepository autoTagJobRepository, AutoTagJobMapper autoTagJobMapper) {
        this.autoTagJobRepository = autoTagJobRepository;
        this.autoTagJobMapper = autoTagJobMapper;
    }

    @Override
    public AutoTagJobDTO save(AutoTagJobDTO autoTagJobDTO) {
        LOG.debug("Request to save AutoTagJob : {}", autoTagJobDTO);
        AutoTagJob autoTagJob = autoTagJobMapper.toEntity(autoTagJobDTO);
        autoTagJob = autoTagJobRepository.save(autoTagJob);
        return autoTagJobMapper.toDto(autoTagJob);
    }

    @Override
    public AutoTagJobDTO update(AutoTagJobDTO autoTagJobDTO) {
        LOG.debug("Request to update AutoTagJob : {}", autoTagJobDTO);
        AutoTagJob autoTagJob = autoTagJobMapper.toEntity(autoTagJobDTO);
        autoTagJob = autoTagJobRepository.save(autoTagJob);
        return autoTagJobMapper.toDto(autoTagJob);
    }

    @Override
    public Optional<AutoTagJobDTO> partialUpdate(AutoTagJobDTO autoTagJobDTO) {
        LOG.debug("Request to partially update AutoTagJob : {}", autoTagJobDTO);

        return autoTagJobRepository
            .findById(autoTagJobDTO.getId())
            .map(existingAutoTagJob -> {
                autoTagJobMapper.partialUpdate(existingAutoTagJob, autoTagJobDTO);

                return existingAutoTagJob;
            })
            .map(autoTagJobRepository::save)
            .map(autoTagJobMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AutoTagJobDTO> findOne(Long id) {
        LOG.debug("Request to get AutoTagJob : {}", id);
        return autoTagJobRepository.findById(id).map(autoTagJobMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete AutoTagJob : {}", id);
        autoTagJobRepository.deleteById(id);
    }
}
