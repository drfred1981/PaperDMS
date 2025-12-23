package fr.smartprod.paperdms.export.service.impl;

import fr.smartprod.paperdms.export.domain.ExportPattern;
import fr.smartprod.paperdms.export.repository.ExportPatternRepository;
import fr.smartprod.paperdms.export.service.ExportPatternService;
import fr.smartprod.paperdms.export.service.dto.ExportPatternDTO;
import fr.smartprod.paperdms.export.service.mapper.ExportPatternMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.export.domain.ExportPattern}.
 */
@Service
@Transactional
public class ExportPatternServiceImpl implements ExportPatternService {

    private static final Logger LOG = LoggerFactory.getLogger(ExportPatternServiceImpl.class);

    private final ExportPatternRepository exportPatternRepository;

    private final ExportPatternMapper exportPatternMapper;

    public ExportPatternServiceImpl(ExportPatternRepository exportPatternRepository, ExportPatternMapper exportPatternMapper) {
        this.exportPatternRepository = exportPatternRepository;
        this.exportPatternMapper = exportPatternMapper;
    }

    @Override
    public ExportPatternDTO save(ExportPatternDTO exportPatternDTO) {
        LOG.debug("Request to save ExportPattern : {}", exportPatternDTO);
        ExportPattern exportPattern = exportPatternMapper.toEntity(exportPatternDTO);
        exportPattern = exportPatternRepository.save(exportPattern);
        return exportPatternMapper.toDto(exportPattern);
    }

    @Override
    public ExportPatternDTO update(ExportPatternDTO exportPatternDTO) {
        LOG.debug("Request to update ExportPattern : {}", exportPatternDTO);
        ExportPattern exportPattern = exportPatternMapper.toEntity(exportPatternDTO);
        exportPattern = exportPatternRepository.save(exportPattern);
        return exportPatternMapper.toDto(exportPattern);
    }

    @Override
    public Optional<ExportPatternDTO> partialUpdate(ExportPatternDTO exportPatternDTO) {
        LOG.debug("Request to partially update ExportPattern : {}", exportPatternDTO);

        return exportPatternRepository
            .findById(exportPatternDTO.getId())
            .map(existingExportPattern -> {
                exportPatternMapper.partialUpdate(existingExportPattern, exportPatternDTO);

                return existingExportPattern;
            })
            .map(exportPatternRepository::save)
            .map(exportPatternMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ExportPatternDTO> findOne(Long id) {
        LOG.debug("Request to get ExportPattern : {}", id);
        return exportPatternRepository.findById(id).map(exportPatternMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ExportPattern : {}", id);
        exportPatternRepository.deleteById(id);
    }
}
