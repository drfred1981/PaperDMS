package fr.smartprod.paperdms.monitoring.service;

import fr.smartprod.paperdms.monitoring.domain.MonitoringSystemHealth;
import fr.smartprod.paperdms.monitoring.repository.MonitoringSystemHealthRepository;
import fr.smartprod.paperdms.monitoring.service.dto.MonitoringSystemHealthDTO;
import fr.smartprod.paperdms.monitoring.service.mapper.MonitoringSystemHealthMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.monitoring.domain.MonitoringSystemHealth}.
 */
@Service
@Transactional
public class MonitoringSystemHealthService {

    private static final Logger LOG = LoggerFactory.getLogger(MonitoringSystemHealthService.class);

    private final MonitoringSystemHealthRepository monitoringSystemHealthRepository;

    private final MonitoringSystemHealthMapper monitoringSystemHealthMapper;

    public MonitoringSystemHealthService(
        MonitoringSystemHealthRepository monitoringSystemHealthRepository,
        MonitoringSystemHealthMapper monitoringSystemHealthMapper
    ) {
        this.monitoringSystemHealthRepository = monitoringSystemHealthRepository;
        this.monitoringSystemHealthMapper = monitoringSystemHealthMapper;
    }

    /**
     * Save a monitoringSystemHealth.
     *
     * @param monitoringSystemHealthDTO the entity to save.
     * @return the persisted entity.
     */
    public MonitoringSystemHealthDTO save(MonitoringSystemHealthDTO monitoringSystemHealthDTO) {
        LOG.debug("Request to save MonitoringSystemHealth : {}", monitoringSystemHealthDTO);
        MonitoringSystemHealth monitoringSystemHealth = monitoringSystemHealthMapper.toEntity(monitoringSystemHealthDTO);
        monitoringSystemHealth = monitoringSystemHealthRepository.save(monitoringSystemHealth);
        return monitoringSystemHealthMapper.toDto(monitoringSystemHealth);
    }

    /**
     * Update a monitoringSystemHealth.
     *
     * @param monitoringSystemHealthDTO the entity to save.
     * @return the persisted entity.
     */
    public MonitoringSystemHealthDTO update(MonitoringSystemHealthDTO monitoringSystemHealthDTO) {
        LOG.debug("Request to update MonitoringSystemHealth : {}", monitoringSystemHealthDTO);
        MonitoringSystemHealth monitoringSystemHealth = monitoringSystemHealthMapper.toEntity(monitoringSystemHealthDTO);
        monitoringSystemHealth = monitoringSystemHealthRepository.save(monitoringSystemHealth);
        return monitoringSystemHealthMapper.toDto(monitoringSystemHealth);
    }

    /**
     * Partially update a monitoringSystemHealth.
     *
     * @param monitoringSystemHealthDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MonitoringSystemHealthDTO> partialUpdate(MonitoringSystemHealthDTO monitoringSystemHealthDTO) {
        LOG.debug("Request to partially update MonitoringSystemHealth : {}", monitoringSystemHealthDTO);

        return monitoringSystemHealthRepository
            .findById(monitoringSystemHealthDTO.getId())
            .map(existingMonitoringSystemHealth -> {
                monitoringSystemHealthMapper.partialUpdate(existingMonitoringSystemHealth, monitoringSystemHealthDTO);

                return existingMonitoringSystemHealth;
            })
            .map(monitoringSystemHealthRepository::save)
            .map(monitoringSystemHealthMapper::toDto);
    }

    /**
     * Get one monitoringSystemHealth by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MonitoringSystemHealthDTO> findOne(Long id) {
        LOG.debug("Request to get MonitoringSystemHealth : {}", id);
        return monitoringSystemHealthRepository.findById(id).map(monitoringSystemHealthMapper::toDto);
    }

    /**
     * Delete the monitoringSystemHealth by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete MonitoringSystemHealth : {}", id);
        monitoringSystemHealthRepository.deleteById(id);
    }
}
