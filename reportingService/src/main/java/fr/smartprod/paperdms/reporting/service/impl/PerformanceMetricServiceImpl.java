package fr.smartprod.paperdms.reporting.service.impl;

import fr.smartprod.paperdms.reporting.domain.PerformanceMetric;
import fr.smartprod.paperdms.reporting.repository.PerformanceMetricRepository;
import fr.smartprod.paperdms.reporting.service.PerformanceMetricService;
import fr.smartprod.paperdms.reporting.service.dto.PerformanceMetricDTO;
import fr.smartprod.paperdms.reporting.service.mapper.PerformanceMetricMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.reporting.domain.PerformanceMetric}.
 */
@Service
@Transactional
public class PerformanceMetricServiceImpl implements PerformanceMetricService {

    private static final Logger LOG = LoggerFactory.getLogger(PerformanceMetricServiceImpl.class);

    private final PerformanceMetricRepository performanceMetricRepository;

    private final PerformanceMetricMapper performanceMetricMapper;

    public PerformanceMetricServiceImpl(
        PerformanceMetricRepository performanceMetricRepository,
        PerformanceMetricMapper performanceMetricMapper
    ) {
        this.performanceMetricRepository = performanceMetricRepository;
        this.performanceMetricMapper = performanceMetricMapper;
    }

    @Override
    public PerformanceMetricDTO save(PerformanceMetricDTO performanceMetricDTO) {
        LOG.debug("Request to save PerformanceMetric : {}", performanceMetricDTO);
        PerformanceMetric performanceMetric = performanceMetricMapper.toEntity(performanceMetricDTO);
        performanceMetric = performanceMetricRepository.save(performanceMetric);
        return performanceMetricMapper.toDto(performanceMetric);
    }

    @Override
    public PerformanceMetricDTO update(PerformanceMetricDTO performanceMetricDTO) {
        LOG.debug("Request to update PerformanceMetric : {}", performanceMetricDTO);
        PerformanceMetric performanceMetric = performanceMetricMapper.toEntity(performanceMetricDTO);
        performanceMetric = performanceMetricRepository.save(performanceMetric);
        return performanceMetricMapper.toDto(performanceMetric);
    }

    @Override
    public Optional<PerformanceMetricDTO> partialUpdate(PerformanceMetricDTO performanceMetricDTO) {
        LOG.debug("Request to partially update PerformanceMetric : {}", performanceMetricDTO);

        return performanceMetricRepository
            .findById(performanceMetricDTO.getId())
            .map(existingPerformanceMetric -> {
                performanceMetricMapper.partialUpdate(existingPerformanceMetric, performanceMetricDTO);

                return existingPerformanceMetric;
            })
            .map(performanceMetricRepository::save)
            .map(performanceMetricMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PerformanceMetricDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all PerformanceMetrics");
        return performanceMetricRepository.findAll(pageable).map(performanceMetricMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PerformanceMetricDTO> findOne(Long id) {
        LOG.debug("Request to get PerformanceMetric : {}", id);
        return performanceMetricRepository.findById(id).map(performanceMetricMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete PerformanceMetric : {}", id);
        performanceMetricRepository.deleteById(id);
    }
}
