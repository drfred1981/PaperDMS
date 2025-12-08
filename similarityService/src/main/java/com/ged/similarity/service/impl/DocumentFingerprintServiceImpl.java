package com.ged.similarity.service.impl;

import com.ged.similarity.domain.DocumentFingerprint;
import com.ged.similarity.repository.DocumentFingerprintRepository;
import com.ged.similarity.service.DocumentFingerprintService;
import com.ged.similarity.service.dto.DocumentFingerprintDTO;
import com.ged.similarity.service.mapper.DocumentFingerprintMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.ged.similarity.domain.DocumentFingerprint}.
 */
@Service
@Transactional
public class DocumentFingerprintServiceImpl implements DocumentFingerprintService {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentFingerprintServiceImpl.class);

    private final DocumentFingerprintRepository documentFingerprintRepository;

    private final DocumentFingerprintMapper documentFingerprintMapper;

    public DocumentFingerprintServiceImpl(
        DocumentFingerprintRepository documentFingerprintRepository,
        DocumentFingerprintMapper documentFingerprintMapper
    ) {
        this.documentFingerprintRepository = documentFingerprintRepository;
        this.documentFingerprintMapper = documentFingerprintMapper;
    }

    @Override
    public DocumentFingerprintDTO save(DocumentFingerprintDTO documentFingerprintDTO) {
        LOG.debug("Request to save DocumentFingerprint : {}", documentFingerprintDTO);
        DocumentFingerprint documentFingerprint = documentFingerprintMapper.toEntity(documentFingerprintDTO);
        documentFingerprint = documentFingerprintRepository.save(documentFingerprint);
        return documentFingerprintMapper.toDto(documentFingerprint);
    }

    @Override
    public DocumentFingerprintDTO update(DocumentFingerprintDTO documentFingerprintDTO) {
        LOG.debug("Request to update DocumentFingerprint : {}", documentFingerprintDTO);
        DocumentFingerprint documentFingerprint = documentFingerprintMapper.toEntity(documentFingerprintDTO);
        documentFingerprint = documentFingerprintRepository.save(documentFingerprint);
        return documentFingerprintMapper.toDto(documentFingerprint);
    }

    @Override
    public Optional<DocumentFingerprintDTO> partialUpdate(DocumentFingerprintDTO documentFingerprintDTO) {
        LOG.debug("Request to partially update DocumentFingerprint : {}", documentFingerprintDTO);

        return documentFingerprintRepository
            .findById(documentFingerprintDTO.getId())
            .map(existingDocumentFingerprint -> {
                documentFingerprintMapper.partialUpdate(existingDocumentFingerprint, documentFingerprintDTO);

                return existingDocumentFingerprint;
            })
            .map(documentFingerprintRepository::save)
            .map(documentFingerprintMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DocumentFingerprintDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all DocumentFingerprints");
        return documentFingerprintRepository.findAll(pageable).map(documentFingerprintMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DocumentFingerprintDTO> findOne(Long id) {
        LOG.debug("Request to get DocumentFingerprint : {}", id);
        return documentFingerprintRepository.findById(id).map(documentFingerprintMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete DocumentFingerprint : {}", id);
        documentFingerprintRepository.deleteById(id);
    }
}
