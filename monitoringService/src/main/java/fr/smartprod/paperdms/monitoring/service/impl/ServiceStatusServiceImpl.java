package fr.smartprod.paperdms.monitoring.service.impl;

import fr.smartprod.paperdms.monitoring.domain.ServiceStatus;
import fr.smartprod.paperdms.monitoring.repository.ServiceStatusRepository;
import fr.smartprod.paperdms.monitoring.service.ServiceStatusService;
import fr.smartprod.paperdms.monitoring.service.dto.ServiceStatusDTO;
import fr.smartprod.paperdms.monitoring.service.mapper.ServiceStatusMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.monitoring.domain.ServiceStatus}.
 */
@Service
@Transactional
public class ServiceStatusServiceImpl implements ServiceStatusService {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceStatusServiceImpl.class);

    private final ServiceStatusRepository serviceStatusRepository;

    private final ServiceStatusMapper serviceStatusMapper;

    public ServiceStatusServiceImpl(ServiceStatusRepository serviceStatusRepository, ServiceStatusMapper serviceStatusMapper) {
        this.serviceStatusRepository = serviceStatusRepository;
        this.serviceStatusMapper = serviceStatusMapper;
    }

    @Override
    public ServiceStatusDTO save(ServiceStatusDTO serviceStatusDTO) {
        LOG.debug("Request to save ServiceStatus : {}", serviceStatusDTO);
        ServiceStatus serviceStatus = serviceStatusMapper.toEntity(serviceStatusDTO);
        serviceStatus = serviceStatusRepository.save(serviceStatus);
        return serviceStatusMapper.toDto(serviceStatus);
    }

    @Override
    public ServiceStatusDTO update(ServiceStatusDTO serviceStatusDTO) {
        LOG.debug("Request to update ServiceStatus : {}", serviceStatusDTO);
        ServiceStatus serviceStatus = serviceStatusMapper.toEntity(serviceStatusDTO);
        serviceStatus = serviceStatusRepository.save(serviceStatus);
        return serviceStatusMapper.toDto(serviceStatus);
    }

    @Override
    public Optional<ServiceStatusDTO> partialUpdate(ServiceStatusDTO serviceStatusDTO) {
        LOG.debug("Request to partially update ServiceStatus : {}", serviceStatusDTO);

        return serviceStatusRepository
            .findById(serviceStatusDTO.getId())
            .map(existingServiceStatus -> {
                serviceStatusMapper.partialUpdate(existingServiceStatus, serviceStatusDTO);

                return existingServiceStatus;
            })
            .map(serviceStatusRepository::save)
            .map(serviceStatusMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ServiceStatusDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all ServiceStatuses");
        return serviceStatusRepository.findAll(pageable).map(serviceStatusMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ServiceStatusDTO> findOne(Long id) {
        LOG.debug("Request to get ServiceStatus : {}", id);
        return serviceStatusRepository.findById(id).map(serviceStatusMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ServiceStatus : {}", id);
        serviceStatusRepository.deleteById(id);
    }
}
