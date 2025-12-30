package fr.smartprod.paperdms.export.service;

import fr.smartprod.paperdms.export.domain.ExportJob;
import fr.smartprod.paperdms.export.repository.ExportJobRepository;
import fr.smartprod.paperdms.export.service.dto.ExportJobDTO;
import fr.smartprod.paperdms.export.service.mapper.ExportJobMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.export.domain.ExportJob}.
 */
@Service
@Transactional
public class ExportJobService {

    private static final Logger LOG = LoggerFactory.getLogger(ExportJobService.class);

    private final ExportJobRepository exportJobRepository;

    private final ExportJobMapper exportJobMapper;

    public ExportJobService(ExportJobRepository exportJobRepository, ExportJobMapper exportJobMapper) {
        this.exportJobRepository = exportJobRepository;
        this.exportJobMapper = exportJobMapper;
    }

    /**
     * Save a exportJob.
     *
     * @param exportJobDTO the entity to save.
     * @return the persisted entity.
     */
    public ExportJobDTO save(ExportJobDTO exportJobDTO) {
        LOG.debug("Request to save ExportJob : {}", exportJobDTO);
        ExportJob exportJob = exportJobMapper.toEntity(exportJobDTO);
        exportJob = exportJobRepository.save(exportJob);
        return exportJobMapper.toDto(exportJob);
    }

    /**
     * Update a exportJob.
     *
     * @param exportJobDTO the entity to save.
     * @return the persisted entity.
     */
    public ExportJobDTO update(ExportJobDTO exportJobDTO) {
        LOG.debug("Request to update ExportJob : {}", exportJobDTO);
        ExportJob exportJob = exportJobMapper.toEntity(exportJobDTO);
        exportJob = exportJobRepository.save(exportJob);
        return exportJobMapper.toDto(exportJob);
    }

    /**
     * Partially update a exportJob.
     *
     * @param exportJobDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ExportJobDTO> partialUpdate(ExportJobDTO exportJobDTO) {
        LOG.debug("Request to partially update ExportJob : {}", exportJobDTO);

        return exportJobRepository
            .findById(exportJobDTO.getId())
            .map(existingExportJob -> {
                exportJobMapper.partialUpdate(existingExportJob, exportJobDTO);

                return existingExportJob;
            })
            .map(exportJobRepository::save)
            .map(exportJobMapper::toDto);
    }

    /**
     * Get one exportJob by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ExportJobDTO> findOne(Long id) {
        LOG.debug("Request to get ExportJob : {}", id);
        return exportJobRepository.findById(id).map(exportJobMapper::toDto);
    }

    /**
     * Delete the exportJob by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ExportJob : {}", id);
        exportJobRepository.deleteById(id);
    }
}
