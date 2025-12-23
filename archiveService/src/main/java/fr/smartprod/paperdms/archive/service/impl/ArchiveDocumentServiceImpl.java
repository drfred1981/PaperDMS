package fr.smartprod.paperdms.archive.service.impl;

import fr.smartprod.paperdms.archive.domain.ArchiveDocument;
import fr.smartprod.paperdms.archive.repository.ArchiveDocumentRepository;
import fr.smartprod.paperdms.archive.service.ArchiveDocumentService;
import fr.smartprod.paperdms.archive.service.dto.ArchiveDocumentDTO;
import fr.smartprod.paperdms.archive.service.mapper.ArchiveDocumentMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.archive.domain.ArchiveDocument}.
 */
@Service
@Transactional
public class ArchiveDocumentServiceImpl implements ArchiveDocumentService {

    private static final Logger LOG = LoggerFactory.getLogger(ArchiveDocumentServiceImpl.class);

    private final ArchiveDocumentRepository archiveDocumentRepository;

    private final ArchiveDocumentMapper archiveDocumentMapper;

    public ArchiveDocumentServiceImpl(ArchiveDocumentRepository archiveDocumentRepository, ArchiveDocumentMapper archiveDocumentMapper) {
        this.archiveDocumentRepository = archiveDocumentRepository;
        this.archiveDocumentMapper = archiveDocumentMapper;
    }

    @Override
    public ArchiveDocumentDTO save(ArchiveDocumentDTO archiveDocumentDTO) {
        LOG.debug("Request to save ArchiveDocument : {}", archiveDocumentDTO);
        ArchiveDocument archiveDocument = archiveDocumentMapper.toEntity(archiveDocumentDTO);
        archiveDocument = archiveDocumentRepository.save(archiveDocument);
        return archiveDocumentMapper.toDto(archiveDocument);
    }

    @Override
    public ArchiveDocumentDTO update(ArchiveDocumentDTO archiveDocumentDTO) {
        LOG.debug("Request to update ArchiveDocument : {}", archiveDocumentDTO);
        ArchiveDocument archiveDocument = archiveDocumentMapper.toEntity(archiveDocumentDTO);
        archiveDocument = archiveDocumentRepository.save(archiveDocument);
        return archiveDocumentMapper.toDto(archiveDocument);
    }

    @Override
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

    @Override
    @Transactional(readOnly = true)
    public Page<ArchiveDocumentDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all ArchiveDocuments");
        return archiveDocumentRepository.findAll(pageable).map(archiveDocumentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ArchiveDocumentDTO> findOne(Long id) {
        LOG.debug("Request to get ArchiveDocument : {}", id);
        return archiveDocumentRepository.findById(id).map(archiveDocumentMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ArchiveDocument : {}", id);
        archiveDocumentRepository.deleteById(id);
    }
}
