package fr.smartprod.paperdms.scan.service.impl;

import fr.smartprod.paperdms.scan.domain.ScanBatch;
import fr.smartprod.paperdms.scan.repository.ScanBatchRepository;
import fr.smartprod.paperdms.scan.service.ScanBatchService;
import fr.smartprod.paperdms.scan.service.dto.ScanBatchDTO;
import fr.smartprod.paperdms.scan.service.mapper.ScanBatchMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.scan.domain.ScanBatch}.
 */
@Service
@Transactional
public class ScanBatchServiceImpl implements ScanBatchService {

    private static final Logger LOG = LoggerFactory.getLogger(ScanBatchServiceImpl.class);

    private final ScanBatchRepository scanBatchRepository;

    private final ScanBatchMapper scanBatchMapper;

    public ScanBatchServiceImpl(ScanBatchRepository scanBatchRepository, ScanBatchMapper scanBatchMapper) {
        this.scanBatchRepository = scanBatchRepository;
        this.scanBatchMapper = scanBatchMapper;
    }

    @Override
    public ScanBatchDTO save(ScanBatchDTO scanBatchDTO) {
        LOG.debug("Request to save ScanBatch : {}", scanBatchDTO);
        ScanBatch scanBatch = scanBatchMapper.toEntity(scanBatchDTO);
        scanBatch = scanBatchRepository.save(scanBatch);
        return scanBatchMapper.toDto(scanBatch);
    }

    @Override
    public ScanBatchDTO update(ScanBatchDTO scanBatchDTO) {
        LOG.debug("Request to update ScanBatch : {}", scanBatchDTO);
        ScanBatch scanBatch = scanBatchMapper.toEntity(scanBatchDTO);
        scanBatch = scanBatchRepository.save(scanBatch);
        return scanBatchMapper.toDto(scanBatch);
    }

    @Override
    public Optional<ScanBatchDTO> partialUpdate(ScanBatchDTO scanBatchDTO) {
        LOG.debug("Request to partially update ScanBatch : {}", scanBatchDTO);

        return scanBatchRepository
            .findById(scanBatchDTO.getId())
            .map(existingScanBatch -> {
                scanBatchMapper.partialUpdate(existingScanBatch, scanBatchDTO);

                return existingScanBatch;
            })
            .map(scanBatchRepository::save)
            .map(scanBatchMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ScanBatchDTO> findOne(Long id) {
        LOG.debug("Request to get ScanBatch : {}", id);
        return scanBatchRepository.findById(id).map(scanBatchMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ScanBatch : {}", id);
        scanBatchRepository.deleteById(id);
    }
}
