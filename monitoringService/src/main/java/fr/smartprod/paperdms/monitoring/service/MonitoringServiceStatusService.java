package fr.smartprod.paperdms.monitoring.service;

import fr.smartprod.paperdms.monitoring.domain.MonitoringServiceStatus;
import fr.smartprod.paperdms.monitoring.repository.MonitoringServiceStatusRepository;
import fr.smartprod.paperdms.monitoring.service.dto.MonitoringServiceStatusDTO;
import fr.smartprod.paperdms.monitoring.service.mapper.MonitoringServiceStatusMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.monitoring.domain.MonitoringServiceStatus}.
 */
@Service
@Transactional
public class MonitoringServiceStatusService {

    private static final Logger LOG = LoggerFactory.getLogger(MonitoringServiceStatusService.class);

    private final MonitoringServiceStatusRepository monitoringServiceStatusRepository;

    private final MonitoringServiceStatusMapper monitoringServiceStatusMapper;

    public MonitoringServiceStatusService(
        MonitoringServiceStatusRepository monitoringServiceStatusRepository,
        MonitoringServiceStatusMapper monitoringServiceStatusMapper
    ) {
        this.monitoringServiceStatusRepository = monitoringServiceStatusRepository;
        this.monitoringServiceStatusMapper = monitoringServiceStatusMapper;
    }

    /**
     * Save a monitoringServiceStatus.
     *
     * @param monitoringServiceStatusDTO the entity to save.
     * @return the persisted entity.
     */
    public MonitoringServiceStatusDTO save(MonitoringServiceStatusDTO monitoringServiceStatusDTO) {
        LOG.debug("Request to save MonitoringServiceStatus : {}", monitoringServiceStatusDTO);
        MonitoringServiceStatus monitoringServiceStatus = monitoringServiceStatusMapper.toEntity(monitoringServiceStatusDTO);
        monitoringServiceStatus = monitoringServiceStatusRepository.save(monitoringServiceStatus);
        return monitoringServiceStatusMapper.toDto(monitoringServiceStatus);
    }

    /**
     * Update a monitoringServiceStatus.
     *
     * @param monitoringServiceStatusDTO the entity to save.
     * @return the persisted entity.
     */
    public MonitoringServiceStatusDTO update(MonitoringServiceStatusDTO monitoringServiceStatusDTO) {
        LOG.debug("Request to update MonitoringServiceStatus : {}", monitoringServiceStatusDTO);
        MonitoringServiceStatus monitoringServiceStatus = monitoringServiceStatusMapper.toEntity(monitoringServiceStatusDTO);
        monitoringServiceStatus = monitoringServiceStatusRepository.save(monitoringServiceStatus);
        return monitoringServiceStatusMapper.toDto(monitoringServiceStatus);
    }

    /**
     * Partially update a monitoringServiceStatus.
     *
     * @param monitoringServiceStatusDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MonitoringServiceStatusDTO> partialUpdate(MonitoringServiceStatusDTO monitoringServiceStatusDTO) {
        LOG.debug("Request to partially update MonitoringServiceStatus : {}", monitoringServiceStatusDTO);

        return monitoringServiceStatusRepository
            .findById(monitoringServiceStatusDTO.getId())
            .map(existingMonitoringServiceStatus -> {
                monitoringServiceStatusMapper.partialUpdate(existingMonitoringServiceStatus, monitoringServiceStatusDTO);

                return existingMonitoringServiceStatus;
            })
            .map(monitoringServiceStatusRepository::save)
            .map(monitoringServiceStatusMapper::toDto);
    }

    /**
     * Get one monitoringServiceStatus by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MonitoringServiceStatusDTO> findOne(Long id) {
        LOG.debug("Request to get MonitoringServiceStatus : {}", id);
        return monitoringServiceStatusRepository.findById(id).map(monitoringServiceStatusMapper::toDto);
    }

    /**
     * Delete the monitoringServiceStatus by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete MonitoringServiceStatus : {}", id);
        monitoringServiceStatusRepository.deleteById(id);
    }
}
