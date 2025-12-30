package fr.smartprod.paperdms.monitoring.service;

import fr.smartprod.paperdms.monitoring.domain.MonitoringMaintenanceTask;
import fr.smartprod.paperdms.monitoring.repository.MonitoringMaintenanceTaskRepository;
import fr.smartprod.paperdms.monitoring.service.dto.MonitoringMaintenanceTaskDTO;
import fr.smartprod.paperdms.monitoring.service.mapper.MonitoringMaintenanceTaskMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.monitoring.domain.MonitoringMaintenanceTask}.
 */
@Service
@Transactional
public class MonitoringMaintenanceTaskService {

    private static final Logger LOG = LoggerFactory.getLogger(MonitoringMaintenanceTaskService.class);

    private final MonitoringMaintenanceTaskRepository monitoringMaintenanceTaskRepository;

    private final MonitoringMaintenanceTaskMapper monitoringMaintenanceTaskMapper;

    public MonitoringMaintenanceTaskService(
        MonitoringMaintenanceTaskRepository monitoringMaintenanceTaskRepository,
        MonitoringMaintenanceTaskMapper monitoringMaintenanceTaskMapper
    ) {
        this.monitoringMaintenanceTaskRepository = monitoringMaintenanceTaskRepository;
        this.monitoringMaintenanceTaskMapper = monitoringMaintenanceTaskMapper;
    }

    /**
     * Save a monitoringMaintenanceTask.
     *
     * @param monitoringMaintenanceTaskDTO the entity to save.
     * @return the persisted entity.
     */
    public MonitoringMaintenanceTaskDTO save(MonitoringMaintenanceTaskDTO monitoringMaintenanceTaskDTO) {
        LOG.debug("Request to save MonitoringMaintenanceTask : {}", monitoringMaintenanceTaskDTO);
        MonitoringMaintenanceTask monitoringMaintenanceTask = monitoringMaintenanceTaskMapper.toEntity(monitoringMaintenanceTaskDTO);
        monitoringMaintenanceTask = monitoringMaintenanceTaskRepository.save(monitoringMaintenanceTask);
        return monitoringMaintenanceTaskMapper.toDto(monitoringMaintenanceTask);
    }

    /**
     * Update a monitoringMaintenanceTask.
     *
     * @param monitoringMaintenanceTaskDTO the entity to save.
     * @return the persisted entity.
     */
    public MonitoringMaintenanceTaskDTO update(MonitoringMaintenanceTaskDTO monitoringMaintenanceTaskDTO) {
        LOG.debug("Request to update MonitoringMaintenanceTask : {}", monitoringMaintenanceTaskDTO);
        MonitoringMaintenanceTask monitoringMaintenanceTask = monitoringMaintenanceTaskMapper.toEntity(monitoringMaintenanceTaskDTO);
        monitoringMaintenanceTask = monitoringMaintenanceTaskRepository.save(monitoringMaintenanceTask);
        return monitoringMaintenanceTaskMapper.toDto(monitoringMaintenanceTask);
    }

    /**
     * Partially update a monitoringMaintenanceTask.
     *
     * @param monitoringMaintenanceTaskDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MonitoringMaintenanceTaskDTO> partialUpdate(MonitoringMaintenanceTaskDTO monitoringMaintenanceTaskDTO) {
        LOG.debug("Request to partially update MonitoringMaintenanceTask : {}", monitoringMaintenanceTaskDTO);

        return monitoringMaintenanceTaskRepository
            .findById(monitoringMaintenanceTaskDTO.getId())
            .map(existingMonitoringMaintenanceTask -> {
                monitoringMaintenanceTaskMapper.partialUpdate(existingMonitoringMaintenanceTask, monitoringMaintenanceTaskDTO);

                return existingMonitoringMaintenanceTask;
            })
            .map(monitoringMaintenanceTaskRepository::save)
            .map(monitoringMaintenanceTaskMapper::toDto);
    }

    /**
     * Get one monitoringMaintenanceTask by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MonitoringMaintenanceTaskDTO> findOne(Long id) {
        LOG.debug("Request to get MonitoringMaintenanceTask : {}", id);
        return monitoringMaintenanceTaskRepository.findById(id).map(monitoringMaintenanceTaskMapper::toDto);
    }

    /**
     * Delete the monitoringMaintenanceTask by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete MonitoringMaintenanceTask : {}", id);
        monitoringMaintenanceTaskRepository.deleteById(id);
    }
}
