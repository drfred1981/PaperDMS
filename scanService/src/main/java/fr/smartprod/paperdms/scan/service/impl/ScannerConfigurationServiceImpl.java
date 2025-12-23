package fr.smartprod.paperdms.scan.service.impl;

import fr.smartprod.paperdms.scan.domain.ScannerConfiguration;
import fr.smartprod.paperdms.scan.repository.ScannerConfigurationRepository;
import fr.smartprod.paperdms.scan.service.ScannerConfigurationService;
import fr.smartprod.paperdms.scan.service.dto.ScannerConfigurationDTO;
import fr.smartprod.paperdms.scan.service.mapper.ScannerConfigurationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.scan.domain.ScannerConfiguration}.
 */
@Service
@Transactional
public class ScannerConfigurationServiceImpl implements ScannerConfigurationService {

    private static final Logger LOG = LoggerFactory.getLogger(ScannerConfigurationServiceImpl.class);

    private final ScannerConfigurationRepository scannerConfigurationRepository;

    private final ScannerConfigurationMapper scannerConfigurationMapper;

    public ScannerConfigurationServiceImpl(
        ScannerConfigurationRepository scannerConfigurationRepository,
        ScannerConfigurationMapper scannerConfigurationMapper
    ) {
        this.scannerConfigurationRepository = scannerConfigurationRepository;
        this.scannerConfigurationMapper = scannerConfigurationMapper;
    }

    @Override
    public ScannerConfigurationDTO save(ScannerConfigurationDTO scannerConfigurationDTO) {
        LOG.debug("Request to save ScannerConfiguration : {}", scannerConfigurationDTO);
        ScannerConfiguration scannerConfiguration = scannerConfigurationMapper.toEntity(scannerConfigurationDTO);
        scannerConfiguration = scannerConfigurationRepository.save(scannerConfiguration);
        return scannerConfigurationMapper.toDto(scannerConfiguration);
    }

    @Override
    public ScannerConfigurationDTO update(ScannerConfigurationDTO scannerConfigurationDTO) {
        LOG.debug("Request to update ScannerConfiguration : {}", scannerConfigurationDTO);
        ScannerConfiguration scannerConfiguration = scannerConfigurationMapper.toEntity(scannerConfigurationDTO);
        scannerConfiguration = scannerConfigurationRepository.save(scannerConfiguration);
        return scannerConfigurationMapper.toDto(scannerConfiguration);
    }

    @Override
    public Optional<ScannerConfigurationDTO> partialUpdate(ScannerConfigurationDTO scannerConfigurationDTO) {
        LOG.debug("Request to partially update ScannerConfiguration : {}", scannerConfigurationDTO);

        return scannerConfigurationRepository
            .findById(scannerConfigurationDTO.getId())
            .map(existingScannerConfiguration -> {
                scannerConfigurationMapper.partialUpdate(existingScannerConfiguration, scannerConfigurationDTO);

                return existingScannerConfiguration;
            })
            .map(scannerConfigurationRepository::save)
            .map(scannerConfigurationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ScannerConfigurationDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all ScannerConfigurations");
        return scannerConfigurationRepository.findAll(pageable).map(scannerConfigurationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ScannerConfigurationDTO> findOne(Long id) {
        LOG.debug("Request to get ScannerConfiguration : {}", id);
        return scannerConfigurationRepository.findById(id).map(scannerConfigurationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ScannerConfiguration : {}", id);
        scannerConfigurationRepository.deleteById(id);
    }
}
