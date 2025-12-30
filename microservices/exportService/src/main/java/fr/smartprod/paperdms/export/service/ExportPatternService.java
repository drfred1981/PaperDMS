package fr.smartprod.paperdms.export.service;

import fr.smartprod.paperdms.export.domain.ExportPattern;
import fr.smartprod.paperdms.export.repository.ExportPatternRepository;
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
public class ExportPatternService {

    private static final Logger LOG = LoggerFactory.getLogger(ExportPatternService.class);

    private final ExportPatternRepository exportPatternRepository;

    private final ExportPatternMapper exportPatternMapper;

    public ExportPatternService(ExportPatternRepository exportPatternRepository, ExportPatternMapper exportPatternMapper) {
        this.exportPatternRepository = exportPatternRepository;
        this.exportPatternMapper = exportPatternMapper;
    }

    /**
     * Save a exportPattern.
     *
     * @param exportPatternDTO the entity to save.
     * @return the persisted entity.
     */
    public ExportPatternDTO save(ExportPatternDTO exportPatternDTO) {
        LOG.debug("Request to save ExportPattern : {}", exportPatternDTO);
        ExportPattern exportPattern = exportPatternMapper.toEntity(exportPatternDTO);
        exportPattern = exportPatternRepository.save(exportPattern);
        return exportPatternMapper.toDto(exportPattern);
    }

    /**
     * Update a exportPattern.
     *
     * @param exportPatternDTO the entity to save.
     * @return the persisted entity.
     */
    public ExportPatternDTO update(ExportPatternDTO exportPatternDTO) {
        LOG.debug("Request to update ExportPattern : {}", exportPatternDTO);
        ExportPattern exportPattern = exportPatternMapper.toEntity(exportPatternDTO);
        exportPattern = exportPatternRepository.save(exportPattern);
        return exportPatternMapper.toDto(exportPattern);
    }

    /**
     * Partially update a exportPattern.
     *
     * @param exportPatternDTO the entity to update partially.
     * @return the persisted entity.
     */
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

    /**
     * Get one exportPattern by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ExportPatternDTO> findOne(Long id) {
        LOG.debug("Request to get ExportPattern : {}", id);
        return exportPatternRepository.findById(id).map(exportPatternMapper::toDto);
    }

    /**
     * Delete the exportPattern by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ExportPattern : {}", id);
        exportPatternRepository.deleteById(id);
    }
}
