package fr.smartprod.paperdms.document.service.impl;

import fr.smartprod.paperdms.document.domain.DocumentVersion;
import fr.smartprod.paperdms.document.repository.DocumentVersionRepository;
import fr.smartprod.paperdms.document.service.DocumentVersionService;
import fr.smartprod.paperdms.document.service.dto.DocumentVersionDTO;
import fr.smartprod.paperdms.document.service.mapper.DocumentVersionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.document.domain.DocumentVersion}.
 */
@Service
@Transactional
public class DocumentVersionServiceImpl implements DocumentVersionService {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentVersionServiceImpl.class);

    private final DocumentVersionRepository documentVersionRepository;

    private final DocumentVersionMapper documentVersionMapper;

    public DocumentVersionServiceImpl(DocumentVersionRepository documentVersionRepository, DocumentVersionMapper documentVersionMapper) {
        this.documentVersionRepository = documentVersionRepository;
        this.documentVersionMapper = documentVersionMapper;
    }

    @Override
    public DocumentVersionDTO save(DocumentVersionDTO documentVersionDTO) {
        LOG.debug("Request to save DocumentVersion : {}", documentVersionDTO);
        DocumentVersion documentVersion = documentVersionMapper.toEntity(documentVersionDTO);
        documentVersion = documentVersionRepository.save(documentVersion);
        return documentVersionMapper.toDto(documentVersion);
    }

    @Override
    public DocumentVersionDTO update(DocumentVersionDTO documentVersionDTO) {
        LOG.debug("Request to update DocumentVersion : {}", documentVersionDTO);
        DocumentVersion documentVersion = documentVersionMapper.toEntity(documentVersionDTO);
        documentVersion = documentVersionRepository.save(documentVersion);
        return documentVersionMapper.toDto(documentVersion);
    }

    @Override
    public Optional<DocumentVersionDTO> partialUpdate(DocumentVersionDTO documentVersionDTO) {
        LOG.debug("Request to partially update DocumentVersion : {}", documentVersionDTO);

        return documentVersionRepository
            .findById(documentVersionDTO.getId())
            .map(existingDocumentVersion -> {
                documentVersionMapper.partialUpdate(existingDocumentVersion, documentVersionDTO);

                return existingDocumentVersion;
            })
            .map(documentVersionRepository::save)
            .map(documentVersionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DocumentVersionDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all DocumentVersions");
        return documentVersionRepository.findAll(pageable).map(documentVersionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DocumentVersionDTO> findOne(Long id) {
        LOG.debug("Request to get DocumentVersion : {}", id);
        return documentVersionRepository.findById(id).map(documentVersionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete DocumentVersion : {}", id);
        documentVersionRepository.deleteById(id);
    }
}
