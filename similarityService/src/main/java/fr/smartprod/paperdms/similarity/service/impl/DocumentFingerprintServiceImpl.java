package fr.smartprod.paperdms.similarity.service.impl;

import fr.smartprod.paperdms.similarity.domain.DocumentFingerprint;
import fr.smartprod.paperdms.similarity.repository.DocumentFingerprintRepository;
import fr.smartprod.paperdms.similarity.repository.search.DocumentFingerprintSearchRepository;
import fr.smartprod.paperdms.similarity.service.DocumentFingerprintService;
import fr.smartprod.paperdms.similarity.service.dto.DocumentFingerprintDTO;
import fr.smartprod.paperdms.similarity.service.mapper.DocumentFingerprintMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.similarity.domain.DocumentFingerprint}.
 */
@Service
@Transactional
public class DocumentFingerprintServiceImpl implements DocumentFingerprintService {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentFingerprintServiceImpl.class);

    private final DocumentFingerprintRepository documentFingerprintRepository;

    private final DocumentFingerprintMapper documentFingerprintMapper;

    private final DocumentFingerprintSearchRepository documentFingerprintSearchRepository;

    public DocumentFingerprintServiceImpl(
        DocumentFingerprintRepository documentFingerprintRepository,
        DocumentFingerprintMapper documentFingerprintMapper,
        DocumentFingerprintSearchRepository documentFingerprintSearchRepository
    ) {
        this.documentFingerprintRepository = documentFingerprintRepository;
        this.documentFingerprintMapper = documentFingerprintMapper;
        this.documentFingerprintSearchRepository = documentFingerprintSearchRepository;
    }

    @Override
    public DocumentFingerprintDTO save(DocumentFingerprintDTO documentFingerprintDTO) {
        LOG.debug("Request to save DocumentFingerprint : {}", documentFingerprintDTO);
        DocumentFingerprint documentFingerprint = documentFingerprintMapper.toEntity(documentFingerprintDTO);
        documentFingerprint = documentFingerprintRepository.save(documentFingerprint);
        documentFingerprintSearchRepository.index(documentFingerprint);
        return documentFingerprintMapper.toDto(documentFingerprint);
    }

    @Override
    public DocumentFingerprintDTO update(DocumentFingerprintDTO documentFingerprintDTO) {
        LOG.debug("Request to update DocumentFingerprint : {}", documentFingerprintDTO);
        DocumentFingerprint documentFingerprint = documentFingerprintMapper.toEntity(documentFingerprintDTO);
        documentFingerprint = documentFingerprintRepository.save(documentFingerprint);
        documentFingerprintSearchRepository.index(documentFingerprint);
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
            .map(savedDocumentFingerprint -> {
                documentFingerprintSearchRepository.index(savedDocumentFingerprint);
                return savedDocumentFingerprint;
            })
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
        documentFingerprintSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DocumentFingerprintDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of DocumentFingerprints for query {}", query);
        return documentFingerprintSearchRepository.search(query, pageable).map(documentFingerprintMapper::toDto);
    }
}
