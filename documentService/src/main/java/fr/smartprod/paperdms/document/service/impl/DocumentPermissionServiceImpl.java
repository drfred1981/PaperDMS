package fr.smartprod.paperdms.document.service.impl;

import fr.smartprod.paperdms.document.domain.DocumentPermission;
import fr.smartprod.paperdms.document.repository.DocumentPermissionRepository;
import fr.smartprod.paperdms.document.service.DocumentPermissionService;
import fr.smartprod.paperdms.document.service.dto.DocumentPermissionDTO;
import fr.smartprod.paperdms.document.service.mapper.DocumentPermissionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.document.domain.DocumentPermission}.
 */
@Service
@Transactional
public class DocumentPermissionServiceImpl implements DocumentPermissionService {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentPermissionServiceImpl.class);

    private final DocumentPermissionRepository documentPermissionRepository;

    private final DocumentPermissionMapper documentPermissionMapper;

    public DocumentPermissionServiceImpl(
        DocumentPermissionRepository documentPermissionRepository,
        DocumentPermissionMapper documentPermissionMapper
    ) {
        this.documentPermissionRepository = documentPermissionRepository;
        this.documentPermissionMapper = documentPermissionMapper;
    }

    @Override
    public DocumentPermissionDTO save(DocumentPermissionDTO documentPermissionDTO) {
        LOG.debug("Request to save DocumentPermission : {}", documentPermissionDTO);
        DocumentPermission documentPermission = documentPermissionMapper.toEntity(documentPermissionDTO);
        documentPermission = documentPermissionRepository.save(documentPermission);
        return documentPermissionMapper.toDto(documentPermission);
    }

    @Override
    public DocumentPermissionDTO update(DocumentPermissionDTO documentPermissionDTO) {
        LOG.debug("Request to update DocumentPermission : {}", documentPermissionDTO);
        DocumentPermission documentPermission = documentPermissionMapper.toEntity(documentPermissionDTO);
        documentPermission = documentPermissionRepository.save(documentPermission);
        return documentPermissionMapper.toDto(documentPermission);
    }

    @Override
    public Optional<DocumentPermissionDTO> partialUpdate(DocumentPermissionDTO documentPermissionDTO) {
        LOG.debug("Request to partially update DocumentPermission : {}", documentPermissionDTO);

        return documentPermissionRepository
            .findById(documentPermissionDTO.getId())
            .map(existingDocumentPermission -> {
                documentPermissionMapper.partialUpdate(existingDocumentPermission, documentPermissionDTO);

                return existingDocumentPermission;
            })
            .map(documentPermissionRepository::save)
            .map(documentPermissionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DocumentPermissionDTO> findOne(Long id) {
        LOG.debug("Request to get DocumentPermission : {}", id);
        return documentPermissionRepository.findById(id).map(documentPermissionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete DocumentPermission : {}", id);
        documentPermissionRepository.deleteById(id);
    }
}
