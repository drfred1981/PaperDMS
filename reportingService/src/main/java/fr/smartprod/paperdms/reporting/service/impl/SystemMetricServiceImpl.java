package fr.smartprod.paperdms.reporting.service.impl;

import fr.smartprod.paperdms.reporting.domain.SystemMetric;
import fr.smartprod.paperdms.reporting.repository.SystemMetricRepository;
import fr.smartprod.paperdms.reporting.service.SystemMetricService;
import fr.smartprod.paperdms.reporting.service.dto.SystemMetricDTO;
import fr.smartprod.paperdms.reporting.service.mapper.SystemMetricMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.reporting.domain.SystemMetric}.
 */
@Service
@Transactional
public class SystemMetricServiceImpl implements SystemMetricService {

    private static final Logger LOG = LoggerFactory.getLogger(SystemMetricServiceImpl.class);

    private final SystemMetricRepository systemMetricRepository;

    private final SystemMetricMapper systemMetricMapper;

    public SystemMetricServiceImpl(SystemMetricRepository systemMetricRepository, SystemMetricMapper systemMetricMapper) {
        this.systemMetricRepository = systemMetricRepository;
        this.systemMetricMapper = systemMetricMapper;
    }

    @Override
    public SystemMetricDTO save(SystemMetricDTO systemMetricDTO) {
        LOG.debug("Request to save SystemMetric : {}", systemMetricDTO);
        SystemMetric systemMetric = systemMetricMapper.toEntity(systemMetricDTO);
        systemMetric = systemMetricRepository.save(systemMetric);
        return systemMetricMapper.toDto(systemMetric);
    }

    @Override
    public SystemMetricDTO update(SystemMetricDTO systemMetricDTO) {
        LOG.debug("Request to update SystemMetric : {}", systemMetricDTO);
        SystemMetric systemMetric = systemMetricMapper.toEntity(systemMetricDTO);
        systemMetric = systemMetricRepository.save(systemMetric);
        return systemMetricMapper.toDto(systemMetric);
    }

    @Override
    public Optional<SystemMetricDTO> partialUpdate(SystemMetricDTO systemMetricDTO) {
        LOG.debug("Request to partially update SystemMetric : {}", systemMetricDTO);

        return systemMetricRepository
            .findById(systemMetricDTO.getId())
            .map(existingSystemMetric -> {
                systemMetricMapper.partialUpdate(existingSystemMetric, systemMetricDTO);

                return existingSystemMetric;
            })
            .map(systemMetricRepository::save)
            .map(systemMetricMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SystemMetricDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all SystemMetrics");
        return systemMetricRepository.findAll(pageable).map(systemMetricMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SystemMetricDTO> findOne(Long id) {
        LOG.debug("Request to get SystemMetric : {}", id);
        return systemMetricRepository.findById(id).map(systemMetricMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete SystemMetric : {}", id);
        systemMetricRepository.deleteById(id);
    }
}
