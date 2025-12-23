package fr.smartprod.paperdms.monitoring.service.impl;

import fr.smartprod.paperdms.monitoring.domain.DocumentWatch;
import fr.smartprod.paperdms.monitoring.repository.DocumentWatchRepository;
import fr.smartprod.paperdms.monitoring.service.DocumentWatchService;
import fr.smartprod.paperdms.monitoring.service.dto.DocumentWatchDTO;
import fr.smartprod.paperdms.monitoring.service.mapper.DocumentWatchMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.monitoring.domain.DocumentWatch}.
 */
@Service
@Transactional
public class DocumentWatchServiceImpl implements DocumentWatchService {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentWatchServiceImpl.class);

    private final DocumentWatchRepository documentWatchRepository;

    private final DocumentWatchMapper documentWatchMapper;

    public DocumentWatchServiceImpl(DocumentWatchRepository documentWatchRepository, DocumentWatchMapper documentWatchMapper) {
        this.documentWatchRepository = documentWatchRepository;
        this.documentWatchMapper = documentWatchMapper;
    }

    @Override
    public DocumentWatchDTO save(DocumentWatchDTO documentWatchDTO) {
        LOG.debug("Request to save DocumentWatch : {}", documentWatchDTO);
        DocumentWatch documentWatch = documentWatchMapper.toEntity(documentWatchDTO);
        documentWatch = documentWatchRepository.save(documentWatch);
        return documentWatchMapper.toDto(documentWatch);
    }

    @Override
    public DocumentWatchDTO update(DocumentWatchDTO documentWatchDTO) {
        LOG.debug("Request to update DocumentWatch : {}", documentWatchDTO);
        DocumentWatch documentWatch = documentWatchMapper.toEntity(documentWatchDTO);
        documentWatch = documentWatchRepository.save(documentWatch);
        return documentWatchMapper.toDto(documentWatch);
    }

    @Override
    public Optional<DocumentWatchDTO> partialUpdate(DocumentWatchDTO documentWatchDTO) {
        LOG.debug("Request to partially update DocumentWatch : {}", documentWatchDTO);

        return documentWatchRepository
            .findById(documentWatchDTO.getId())
            .map(existingDocumentWatch -> {
                documentWatchMapper.partialUpdate(existingDocumentWatch, documentWatchDTO);

                return existingDocumentWatch;
            })
            .map(documentWatchRepository::save)
            .map(documentWatchMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DocumentWatchDTO> findOne(Long id) {
        LOG.debug("Request to get DocumentWatch : {}", id);
        return documentWatchRepository.findById(id).map(documentWatchMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete DocumentWatch : {}", id);
        documentWatchRepository.deleteById(id);
    }
}
