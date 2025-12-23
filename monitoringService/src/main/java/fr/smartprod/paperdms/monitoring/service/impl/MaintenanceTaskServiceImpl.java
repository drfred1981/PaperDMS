package fr.smartprod.paperdms.monitoring.service.impl;

import fr.smartprod.paperdms.monitoring.domain.MaintenanceTask;
import fr.smartprod.paperdms.monitoring.repository.MaintenanceTaskRepository;
import fr.smartprod.paperdms.monitoring.service.MaintenanceTaskService;
import fr.smartprod.paperdms.monitoring.service.dto.MaintenanceTaskDTO;
import fr.smartprod.paperdms.monitoring.service.mapper.MaintenanceTaskMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.monitoring.domain.MaintenanceTask}.
 */
@Service
@Transactional
public class MaintenanceTaskServiceImpl implements MaintenanceTaskService {

    private static final Logger LOG = LoggerFactory.getLogger(MaintenanceTaskServiceImpl.class);

    private final MaintenanceTaskRepository maintenanceTaskRepository;

    private final MaintenanceTaskMapper maintenanceTaskMapper;

    public MaintenanceTaskServiceImpl(MaintenanceTaskRepository maintenanceTaskRepository, MaintenanceTaskMapper maintenanceTaskMapper) {
        this.maintenanceTaskRepository = maintenanceTaskRepository;
        this.maintenanceTaskMapper = maintenanceTaskMapper;
    }

    @Override
    public MaintenanceTaskDTO save(MaintenanceTaskDTO maintenanceTaskDTO) {
        LOG.debug("Request to save MaintenanceTask : {}", maintenanceTaskDTO);
        MaintenanceTask maintenanceTask = maintenanceTaskMapper.toEntity(maintenanceTaskDTO);
        maintenanceTask = maintenanceTaskRepository.save(maintenanceTask);
        return maintenanceTaskMapper.toDto(maintenanceTask);
    }

    @Override
    public MaintenanceTaskDTO update(MaintenanceTaskDTO maintenanceTaskDTO) {
        LOG.debug("Request to update MaintenanceTask : {}", maintenanceTaskDTO);
        MaintenanceTask maintenanceTask = maintenanceTaskMapper.toEntity(maintenanceTaskDTO);
        maintenanceTask = maintenanceTaskRepository.save(maintenanceTask);
        return maintenanceTaskMapper.toDto(maintenanceTask);
    }

    @Override
    public Optional<MaintenanceTaskDTO> partialUpdate(MaintenanceTaskDTO maintenanceTaskDTO) {
        LOG.debug("Request to partially update MaintenanceTask : {}", maintenanceTaskDTO);

        return maintenanceTaskRepository
            .findById(maintenanceTaskDTO.getId())
            .map(existingMaintenanceTask -> {
                maintenanceTaskMapper.partialUpdate(existingMaintenanceTask, maintenanceTaskDTO);

                return existingMaintenanceTask;
            })
            .map(maintenanceTaskRepository::save)
            .map(maintenanceTaskMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MaintenanceTaskDTO> findOne(Long id) {
        LOG.debug("Request to get MaintenanceTask : {}", id);
        return maintenanceTaskRepository.findById(id).map(maintenanceTaskMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete MaintenanceTask : {}", id);
        maintenanceTaskRepository.deleteById(id);
    }
}
