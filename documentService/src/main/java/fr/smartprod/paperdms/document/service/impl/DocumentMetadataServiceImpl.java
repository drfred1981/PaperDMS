package fr.smartprod.paperdms.document.service.impl;

import fr.smartprod.paperdms.document.domain.DocumentMetadata;
import fr.smartprod.paperdms.document.repository.DocumentMetadataRepository;
import fr.smartprod.paperdms.document.service.DocumentMetadataService;
import fr.smartprod.paperdms.document.service.dto.DocumentMetadataDTO;
import fr.smartprod.paperdms.document.service.mapper.DocumentMetadataMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.document.domain.DocumentMetadata}.
 */
@Service
@Transactional
public class DocumentMetadataServiceImpl implements DocumentMetadataService {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentMetadataServiceImpl.class);

    private final DocumentMetadataRepository documentMetadataRepository;

    private final DocumentMetadataMapper documentMetadataMapper;

    public DocumentMetadataServiceImpl(
        DocumentMetadataRepository documentMetadataRepository,
        DocumentMetadataMapper documentMetadataMapper
    ) {
        this.documentMetadataRepository = documentMetadataRepository;
        this.documentMetadataMapper = documentMetadataMapper;
    }

    @Override
    public DocumentMetadataDTO save(DocumentMetadataDTO documentMetadataDTO) {
        LOG.debug("Request to save DocumentMetadata : {}", documentMetadataDTO);
        DocumentMetadata documentMetadata = documentMetadataMapper.toEntity(documentMetadataDTO);
        documentMetadata = documentMetadataRepository.save(documentMetadata);
        return documentMetadataMapper.toDto(documentMetadata);
    }

    @Override
    public DocumentMetadataDTO update(DocumentMetadataDTO documentMetadataDTO) {
        LOG.debug("Request to update DocumentMetadata : {}", documentMetadataDTO);
        DocumentMetadata documentMetadata = documentMetadataMapper.toEntity(documentMetadataDTO);
        documentMetadata = documentMetadataRepository.save(documentMetadata);
        return documentMetadataMapper.toDto(documentMetadata);
    }

    @Override
    public Optional<DocumentMetadataDTO> partialUpdate(DocumentMetadataDTO documentMetadataDTO) {
        LOG.debug("Request to partially update DocumentMetadata : {}", documentMetadataDTO);

        return documentMetadataRepository
            .findById(documentMetadataDTO.getId())
            .map(existingDocumentMetadata -> {
                documentMetadataMapper.partialUpdate(existingDocumentMetadata, documentMetadataDTO);

                return existingDocumentMetadata;
            })
            .map(documentMetadataRepository::save)
            .map(documentMetadataMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DocumentMetadataDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all DocumentMetadata");
        return documentMetadataRepository.findAll(pageable).map(documentMetadataMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DocumentMetadataDTO> findOne(Long id) {
        LOG.debug("Request to get DocumentMetadata : {}", id);
        return documentMetadataRepository.findById(id).map(documentMetadataMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete DocumentMetadata : {}", id);
        documentMetadataRepository.deleteById(id);
    }
}
