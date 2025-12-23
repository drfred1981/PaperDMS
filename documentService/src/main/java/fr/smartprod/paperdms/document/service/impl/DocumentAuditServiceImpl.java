package fr.smartprod.paperdms.document.service.impl;

import fr.smartprod.paperdms.document.domain.DocumentAudit;
import fr.smartprod.paperdms.document.repository.DocumentAuditRepository;
import fr.smartprod.paperdms.document.service.DocumentAuditService;
import fr.smartprod.paperdms.document.service.dto.DocumentAuditDTO;
import fr.smartprod.paperdms.document.service.mapper.DocumentAuditMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.document.domain.DocumentAudit}.
 */
@Service
@Transactional
public class DocumentAuditServiceImpl implements DocumentAuditService {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentAuditServiceImpl.class);

    private final DocumentAuditRepository documentAuditRepository;

    private final DocumentAuditMapper documentAuditMapper;

    public DocumentAuditServiceImpl(DocumentAuditRepository documentAuditRepository, DocumentAuditMapper documentAuditMapper) {
        this.documentAuditRepository = documentAuditRepository;
        this.documentAuditMapper = documentAuditMapper;
    }

    @Override
    public DocumentAuditDTO save(DocumentAuditDTO documentAuditDTO) {
        LOG.debug("Request to save DocumentAudit : {}", documentAuditDTO);
        DocumentAudit documentAudit = documentAuditMapper.toEntity(documentAuditDTO);
        documentAudit = documentAuditRepository.save(documentAudit);
        return documentAuditMapper.toDto(documentAudit);
    }

    @Override
    public DocumentAuditDTO update(DocumentAuditDTO documentAuditDTO) {
        LOG.debug("Request to update DocumentAudit : {}", documentAuditDTO);
        DocumentAudit documentAudit = documentAuditMapper.toEntity(documentAuditDTO);
        documentAudit = documentAuditRepository.save(documentAudit);
        return documentAuditMapper.toDto(documentAudit);
    }

    @Override
    public Optional<DocumentAuditDTO> partialUpdate(DocumentAuditDTO documentAuditDTO) {
        LOG.debug("Request to partially update DocumentAudit : {}", documentAuditDTO);

        return documentAuditRepository
            .findById(documentAuditDTO.getId())
            .map(existingDocumentAudit -> {
                documentAuditMapper.partialUpdate(existingDocumentAudit, documentAuditDTO);

                return existingDocumentAudit;
            })
            .map(documentAuditRepository::save)
            .map(documentAuditMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DocumentAuditDTO> findOne(Long id) {
        LOG.debug("Request to get DocumentAudit : {}", id);
        return documentAuditRepository.findById(id).map(documentAuditMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete DocumentAudit : {}", id);
        documentAuditRepository.deleteById(id);
    }
}
