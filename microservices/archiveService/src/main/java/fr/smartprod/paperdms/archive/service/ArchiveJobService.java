package fr.smartprod.paperdms.archive.service;

import fr.smartprod.paperdms.archive.domain.ArchiveJob;
import fr.smartprod.paperdms.archive.repository.ArchiveJobRepository;
import fr.smartprod.paperdms.archive.service.dto.ArchiveJobDTO;
import fr.smartprod.paperdms.archive.service.mapper.ArchiveJobMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.archive.domain.ArchiveJob}.
 */
@Service
@Transactional
public class ArchiveJobService {

    private static final Logger LOG = LoggerFactory.getLogger(ArchiveJobService.class);

    private final ArchiveJobRepository archiveJobRepository;

    private final ArchiveJobMapper archiveJobMapper;

    public ArchiveJobService(ArchiveJobRepository archiveJobRepository, ArchiveJobMapper archiveJobMapper) {
        this.archiveJobRepository = archiveJobRepository;
        this.archiveJobMapper = archiveJobMapper;
    }

    /**
     * Save a archiveJob.
     *
     * @param archiveJobDTO the entity to save.
     * @return the persisted entity.
     */
    public ArchiveJobDTO save(ArchiveJobDTO archiveJobDTO) {
        LOG.debug("Request to save ArchiveJob : {}", archiveJobDTO);
        ArchiveJob archiveJob = archiveJobMapper.toEntity(archiveJobDTO);
        archiveJob = archiveJobRepository.save(archiveJob);
        return archiveJobMapper.toDto(archiveJob);
    }

    /**
     * Update a archiveJob.
     *
     * @param archiveJobDTO the entity to save.
     * @return the persisted entity.
     */
    public ArchiveJobDTO update(ArchiveJobDTO archiveJobDTO) {
        LOG.debug("Request to update ArchiveJob : {}", archiveJobDTO);
        ArchiveJob archiveJob = archiveJobMapper.toEntity(archiveJobDTO);
        archiveJob = archiveJobRepository.save(archiveJob);
        return archiveJobMapper.toDto(archiveJob);
    }

    /**
     * Partially update a archiveJob.
     *
     * @param archiveJobDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ArchiveJobDTO> partialUpdate(ArchiveJobDTO archiveJobDTO) {
        LOG.debug("Request to partially update ArchiveJob : {}", archiveJobDTO);

        return archiveJobRepository
            .findById(archiveJobDTO.getId())
            .map(existingArchiveJob -> {
                archiveJobMapper.partialUpdate(existingArchiveJob, archiveJobDTO);

                return existingArchiveJob;
            })
            .map(archiveJobRepository::save)
            .map(archiveJobMapper::toDto);
    }

    /**
     * Get one archiveJob by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ArchiveJobDTO> findOne(Long id) {
        LOG.debug("Request to get ArchiveJob : {}", id);
        return archiveJobRepository.findById(id).map(archiveJobMapper::toDto);
    }

    /**
     * Delete the archiveJob by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ArchiveJob : {}", id);
        archiveJobRepository.deleteById(id);
    }
}
