package fr.smartprod.paperdms.archive.service;

import fr.smartprod.paperdms.archive.domain.ArchiveDocument;
import fr.smartprod.paperdms.archive.repository.ArchiveDocumentRepository;
import fr.smartprod.paperdms.archive.service.dto.ArchiveDocumentDTO;
import fr.smartprod.paperdms.archive.service.mapper.ArchiveDocumentMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.archive.domain.ArchiveDocument}.
 */
@Service
@Transactional
public class ArchiveDocumentService {

    private static final Logger LOG = LoggerFactory.getLogger(ArchiveDocumentService.class);

    private final ArchiveDocumentRepository archiveDocumentRepository;

    private final ArchiveDocumentMapper archiveDocumentMapper;

    public ArchiveDocumentService(ArchiveDocumentRepository archiveDocumentRepository, ArchiveDocumentMapper archiveDocumentMapper) {
        this.archiveDocumentRepository = archiveDocumentRepository;
        this.archiveDocumentMapper = archiveDocumentMapper;
    }

    /**
     * Save a archiveDocument.
     *
     * @param archiveDocumentDTO the entity to save.
     * @return the persisted entity.
     */
    public ArchiveDocumentDTO save(ArchiveDocumentDTO archiveDocumentDTO) {
        LOG.debug("Request to save ArchiveDocument : {}", archiveDocumentDTO);
        ArchiveDocument archiveDocument = archiveDocumentMapper.toEntity(archiveDocumentDTO);
        archiveDocument = archiveDocumentRepository.save(archiveDocument);
        return archiveDocumentMapper.toDto(archiveDocument);
    }

    /**
     * Update a archiveDocument.
     *
     * @param archiveDocumentDTO the entity to save.
     * @return the persisted entity.
     */
    public ArchiveDocumentDTO update(ArchiveDocumentDTO archiveDocumentDTO) {
        LOG.debug("Request to update ArchiveDocument : {}", archiveDocumentDTO);
        ArchiveDocument archiveDocument = archiveDocumentMapper.toEntity(archiveDocumentDTO);
        archiveDocument = archiveDocumentRepository.save(archiveDocument);
        return archiveDocumentMapper.toDto(archiveDocument);
    }

    /**
     * Partially update a archiveDocument.
     *
     * @param archiveDocumentDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ArchiveDocumentDTO> partialUpdate(ArchiveDocumentDTO archiveDocumentDTO) {
        LOG.debug("Request to partially update ArchiveDocument : {}", archiveDocumentDTO);

        return archiveDocumentRepository
            .findById(archiveDocumentDTO.getId())
            .map(existingArchiveDocument -> {
                archiveDocumentMapper.partialUpdate(existingArchiveDocument, archiveDocumentDTO);

                return existingArchiveDocument;
            })
            .map(archiveDocumentRepository::save)
            .map(archiveDocumentMapper::toDto);
    }

    /**
     * Get one archiveDocument by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ArchiveDocumentDTO> findOne(Long id) {
        LOG.debug("Request to get ArchiveDocument : {}", id);
        return archiveDocumentRepository.findById(id).map(archiveDocumentMapper::toDto);
    }

    /**
     * Delete the archiveDocument by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ArchiveDocument : {}", id);
        archiveDocumentRepository.deleteById(id);
    }
}
