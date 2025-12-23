package fr.smartprod.paperdms.document.service.impl;

import fr.smartprod.paperdms.document.domain.DocumentTypeField;
import fr.smartprod.paperdms.document.repository.DocumentTypeFieldRepository;
import fr.smartprod.paperdms.document.service.DocumentTypeFieldService;
import fr.smartprod.paperdms.document.service.dto.DocumentTypeFieldDTO;
import fr.smartprod.paperdms.document.service.mapper.DocumentTypeFieldMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.document.domain.DocumentTypeField}.
 */
@Service
@Transactional
public class DocumentTypeFieldServiceImpl implements DocumentTypeFieldService {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentTypeFieldServiceImpl.class);

    private final DocumentTypeFieldRepository documentTypeFieldRepository;

    private final DocumentTypeFieldMapper documentTypeFieldMapper;

    public DocumentTypeFieldServiceImpl(
        DocumentTypeFieldRepository documentTypeFieldRepository,
        DocumentTypeFieldMapper documentTypeFieldMapper
    ) {
        this.documentTypeFieldRepository = documentTypeFieldRepository;
        this.documentTypeFieldMapper = documentTypeFieldMapper;
    }

    @Override
    public DocumentTypeFieldDTO save(DocumentTypeFieldDTO documentTypeFieldDTO) {
        LOG.debug("Request to save DocumentTypeField : {}", documentTypeFieldDTO);
        DocumentTypeField documentTypeField = documentTypeFieldMapper.toEntity(documentTypeFieldDTO);
        documentTypeField = documentTypeFieldRepository.save(documentTypeField);
        return documentTypeFieldMapper.toDto(documentTypeField);
    }

    @Override
    public DocumentTypeFieldDTO update(DocumentTypeFieldDTO documentTypeFieldDTO) {
        LOG.debug("Request to update DocumentTypeField : {}", documentTypeFieldDTO);
        DocumentTypeField documentTypeField = documentTypeFieldMapper.toEntity(documentTypeFieldDTO);
        documentTypeField = documentTypeFieldRepository.save(documentTypeField);
        return documentTypeFieldMapper.toDto(documentTypeField);
    }

    @Override
    public Optional<DocumentTypeFieldDTO> partialUpdate(DocumentTypeFieldDTO documentTypeFieldDTO) {
        LOG.debug("Request to partially update DocumentTypeField : {}", documentTypeFieldDTO);

        return documentTypeFieldRepository
            .findById(documentTypeFieldDTO.getId())
            .map(existingDocumentTypeField -> {
                documentTypeFieldMapper.partialUpdate(existingDocumentTypeField, documentTypeFieldDTO);

                return existingDocumentTypeField;
            })
            .map(documentTypeFieldRepository::save)
            .map(documentTypeFieldMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DocumentTypeFieldDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all DocumentTypeFields");
        return documentTypeFieldRepository.findAll(pageable).map(documentTypeFieldMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DocumentTypeFieldDTO> findOne(Long id) {
        LOG.debug("Request to get DocumentTypeField : {}", id);
        return documentTypeFieldRepository.findById(id).map(documentTypeFieldMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete DocumentTypeField : {}", id);
        documentTypeFieldRepository.deleteById(id);
    }
}
