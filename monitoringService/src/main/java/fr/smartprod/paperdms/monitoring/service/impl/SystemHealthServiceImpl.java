package fr.smartprod.paperdms.monitoring.service.impl;

import fr.smartprod.paperdms.monitoring.domain.SystemHealth;
import fr.smartprod.paperdms.monitoring.repository.SystemHealthRepository;
import fr.smartprod.paperdms.monitoring.service.SystemHealthService;
import fr.smartprod.paperdms.monitoring.service.dto.SystemHealthDTO;
import fr.smartprod.paperdms.monitoring.service.mapper.SystemHealthMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.monitoring.domain.SystemHealth}.
 */
@Service
@Transactional
public class SystemHealthServiceImpl implements SystemHealthService {

    private static final Logger LOG = LoggerFactory.getLogger(SystemHealthServiceImpl.class);

    private final SystemHealthRepository systemHealthRepository;

    private final SystemHealthMapper systemHealthMapper;

    public SystemHealthServiceImpl(SystemHealthRepository systemHealthRepository, SystemHealthMapper systemHealthMapper) {
        this.systemHealthRepository = systemHealthRepository;
        this.systemHealthMapper = systemHealthMapper;
    }

    @Override
    public SystemHealthDTO save(SystemHealthDTO systemHealthDTO) {
        LOG.debug("Request to save SystemHealth : {}", systemHealthDTO);
        SystemHealth systemHealth = systemHealthMapper.toEntity(systemHealthDTO);
        systemHealth = systemHealthRepository.save(systemHealth);
        return systemHealthMapper.toDto(systemHealth);
    }

    @Override
    public SystemHealthDTO update(SystemHealthDTO systemHealthDTO) {
        LOG.debug("Request to update SystemHealth : {}", systemHealthDTO);
        SystemHealth systemHealth = systemHealthMapper.toEntity(systemHealthDTO);
        systemHealth = systemHealthRepository.save(systemHealth);
        return systemHealthMapper.toDto(systemHealth);
    }

    @Override
    public Optional<SystemHealthDTO> partialUpdate(SystemHealthDTO systemHealthDTO) {
        LOG.debug("Request to partially update SystemHealth : {}", systemHealthDTO);

        return systemHealthRepository
            .findById(systemHealthDTO.getId())
            .map(existingSystemHealth -> {
                systemHealthMapper.partialUpdate(existingSystemHealth, systemHealthDTO);

                return existingSystemHealth;
            })
            .map(systemHealthRepository::save)
            .map(systemHealthMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SystemHealthDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all SystemHealths");
        return systemHealthRepository.findAll(pageable).map(systemHealthMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SystemHealthDTO> findOne(Long id) {
        LOG.debug("Request to get SystemHealth : {}", id);
        return systemHealthRepository.findById(id).map(systemHealthMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete SystemHealth : {}", id);
        systemHealthRepository.deleteById(id);
    }
}
