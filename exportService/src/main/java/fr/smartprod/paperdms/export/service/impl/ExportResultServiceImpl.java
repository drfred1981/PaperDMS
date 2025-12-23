package fr.smartprod.paperdms.export.service.impl;

import fr.smartprod.paperdms.export.domain.ExportResult;
import fr.smartprod.paperdms.export.repository.ExportResultRepository;
import fr.smartprod.paperdms.export.service.ExportResultService;
import fr.smartprod.paperdms.export.service.dto.ExportResultDTO;
import fr.smartprod.paperdms.export.service.mapper.ExportResultMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.export.domain.ExportResult}.
 */
@Service
@Transactional
public class ExportResultServiceImpl implements ExportResultService {

    private static final Logger LOG = LoggerFactory.getLogger(ExportResultServiceImpl.class);

    private final ExportResultRepository exportResultRepository;

    private final ExportResultMapper exportResultMapper;

    public ExportResultServiceImpl(ExportResultRepository exportResultRepository, ExportResultMapper exportResultMapper) {
        this.exportResultRepository = exportResultRepository;
        this.exportResultMapper = exportResultMapper;
    }

    @Override
    public ExportResultDTO save(ExportResultDTO exportResultDTO) {
        LOG.debug("Request to save ExportResult : {}", exportResultDTO);
        ExportResult exportResult = exportResultMapper.toEntity(exportResultDTO);
        exportResult = exportResultRepository.save(exportResult);
        return exportResultMapper.toDto(exportResult);
    }

    @Override
    public ExportResultDTO update(ExportResultDTO exportResultDTO) {
        LOG.debug("Request to update ExportResult : {}", exportResultDTO);
        ExportResult exportResult = exportResultMapper.toEntity(exportResultDTO);
        exportResult = exportResultRepository.save(exportResult);
        return exportResultMapper.toDto(exportResult);
    }

    @Override
    public Optional<ExportResultDTO> partialUpdate(ExportResultDTO exportResultDTO) {
        LOG.debug("Request to partially update ExportResult : {}", exportResultDTO);

        return exportResultRepository
            .findById(exportResultDTO.getId())
            .map(existingExportResult -> {
                exportResultMapper.partialUpdate(existingExportResult, exportResultDTO);

                return existingExportResult;
            })
            .map(exportResultRepository::save)
            .map(exportResultMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ExportResultDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all ExportResults");
        return exportResultRepository.findAll(pageable).map(exportResultMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ExportResultDTO> findOne(Long id) {
        LOG.debug("Request to get ExportResult : {}", id);
        return exportResultRepository.findById(id).map(exportResultMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ExportResult : {}", id);
        exportResultRepository.deleteById(id);
    }
}
