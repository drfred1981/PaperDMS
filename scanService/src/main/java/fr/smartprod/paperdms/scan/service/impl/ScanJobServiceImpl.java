package fr.smartprod.paperdms.scan.service.impl;

import fr.smartprod.paperdms.scan.domain.ScanJob;
import fr.smartprod.paperdms.scan.repository.ScanJobRepository;
import fr.smartprod.paperdms.scan.service.ScanJobService;
import fr.smartprod.paperdms.scan.service.dto.ScanJobDTO;
import fr.smartprod.paperdms.scan.service.mapper.ScanJobMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.scan.domain.ScanJob}.
 */
@Service
@Transactional
public class ScanJobServiceImpl implements ScanJobService {

    private static final Logger LOG = LoggerFactory.getLogger(ScanJobServiceImpl.class);

    private final ScanJobRepository scanJobRepository;

    private final ScanJobMapper scanJobMapper;

    public ScanJobServiceImpl(ScanJobRepository scanJobRepository, ScanJobMapper scanJobMapper) {
        this.scanJobRepository = scanJobRepository;
        this.scanJobMapper = scanJobMapper;
    }

    @Override
    public ScanJobDTO save(ScanJobDTO scanJobDTO) {
        LOG.debug("Request to save ScanJob : {}", scanJobDTO);
        ScanJob scanJob = scanJobMapper.toEntity(scanJobDTO);
        scanJob = scanJobRepository.save(scanJob);
        return scanJobMapper.toDto(scanJob);
    }

    @Override
    public ScanJobDTO update(ScanJobDTO scanJobDTO) {
        LOG.debug("Request to update ScanJob : {}", scanJobDTO);
        ScanJob scanJob = scanJobMapper.toEntity(scanJobDTO);
        scanJob = scanJobRepository.save(scanJob);
        return scanJobMapper.toDto(scanJob);
    }

    @Override
    public Optional<ScanJobDTO> partialUpdate(ScanJobDTO scanJobDTO) {
        LOG.debug("Request to partially update ScanJob : {}", scanJobDTO);

        return scanJobRepository
            .findById(scanJobDTO.getId())
            .map(existingScanJob -> {
                scanJobMapper.partialUpdate(existingScanJob, scanJobDTO);

                return existingScanJob;
            })
            .map(scanJobRepository::save)
            .map(scanJobMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ScanJobDTO> findOne(Long id) {
        LOG.debug("Request to get ScanJob : {}", id);
        return scanJobRepository.findById(id).map(scanJobMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ScanJob : {}", id);
        scanJobRepository.deleteById(id);
    }
}
