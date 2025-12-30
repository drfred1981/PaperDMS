package fr.smartprod.paperdms.export.service;

import fr.smartprod.paperdms.export.domain.ExportResult;
import fr.smartprod.paperdms.export.repository.ExportResultRepository;
import fr.smartprod.paperdms.export.service.dto.ExportResultDTO;
import fr.smartprod.paperdms.export.service.mapper.ExportResultMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.export.domain.ExportResult}.
 */
@Service
@Transactional
public class ExportResultService {

    private static final Logger LOG = LoggerFactory.getLogger(ExportResultService.class);

    private final ExportResultRepository exportResultRepository;

    private final ExportResultMapper exportResultMapper;

    public ExportResultService(ExportResultRepository exportResultRepository, ExportResultMapper exportResultMapper) {
        this.exportResultRepository = exportResultRepository;
        this.exportResultMapper = exportResultMapper;
    }

    /**
     * Save a exportResult.
     *
     * @param exportResultDTO the entity to save.
     * @return the persisted entity.
     */
    public ExportResultDTO save(ExportResultDTO exportResultDTO) {
        LOG.debug("Request to save ExportResult : {}", exportResultDTO);
        ExportResult exportResult = exportResultMapper.toEntity(exportResultDTO);
        exportResult = exportResultRepository.save(exportResult);
        return exportResultMapper.toDto(exportResult);
    }

    /**
     * Update a exportResult.
     *
     * @param exportResultDTO the entity to save.
     * @return the persisted entity.
     */
    public ExportResultDTO update(ExportResultDTO exportResultDTO) {
        LOG.debug("Request to update ExportResult : {}", exportResultDTO);
        ExportResult exportResult = exportResultMapper.toEntity(exportResultDTO);
        exportResult = exportResultRepository.save(exportResult);
        return exportResultMapper.toDto(exportResult);
    }

    /**
     * Partially update a exportResult.
     *
     * @param exportResultDTO the entity to update partially.
     * @return the persisted entity.
     */
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

    /**
     * Get one exportResult by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ExportResultDTO> findOne(Long id) {
        LOG.debug("Request to get ExportResult : {}", id);
        return exportResultRepository.findById(id).map(exportResultMapper::toDto);
    }

    /**
     * Delete the exportResult by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ExportResult : {}", id);
        exportResultRepository.deleteById(id);
    }
}
