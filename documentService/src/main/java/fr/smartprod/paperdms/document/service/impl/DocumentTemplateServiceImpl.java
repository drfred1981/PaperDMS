package fr.smartprod.paperdms.document.service.impl;

import fr.smartprod.paperdms.document.domain.DocumentTemplate;
import fr.smartprod.paperdms.document.repository.DocumentTemplateRepository;
import fr.smartprod.paperdms.document.service.DocumentTemplateService;
import fr.smartprod.paperdms.document.service.dto.DocumentTemplateDTO;
import fr.smartprod.paperdms.document.service.mapper.DocumentTemplateMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.document.domain.DocumentTemplate}.
 */
@Service
@Transactional
public class DocumentTemplateServiceImpl implements DocumentTemplateService {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentTemplateServiceImpl.class);

    private final DocumentTemplateRepository documentTemplateRepository;

    private final DocumentTemplateMapper documentTemplateMapper;

    public DocumentTemplateServiceImpl(
        DocumentTemplateRepository documentTemplateRepository,
        DocumentTemplateMapper documentTemplateMapper
    ) {
        this.documentTemplateRepository = documentTemplateRepository;
        this.documentTemplateMapper = documentTemplateMapper;
    }

    @Override
    public DocumentTemplateDTO save(DocumentTemplateDTO documentTemplateDTO) {
        LOG.debug("Request to save DocumentTemplate : {}", documentTemplateDTO);
        DocumentTemplate documentTemplate = documentTemplateMapper.toEntity(documentTemplateDTO);
        documentTemplate = documentTemplateRepository.save(documentTemplate);
        return documentTemplateMapper.toDto(documentTemplate);
    }

    @Override
    public DocumentTemplateDTO update(DocumentTemplateDTO documentTemplateDTO) {
        LOG.debug("Request to update DocumentTemplate : {}", documentTemplateDTO);
        DocumentTemplate documentTemplate = documentTemplateMapper.toEntity(documentTemplateDTO);
        documentTemplate = documentTemplateRepository.save(documentTemplate);
        return documentTemplateMapper.toDto(documentTemplate);
    }

    @Override
    public Optional<DocumentTemplateDTO> partialUpdate(DocumentTemplateDTO documentTemplateDTO) {
        LOG.debug("Request to partially update DocumentTemplate : {}", documentTemplateDTO);

        return documentTemplateRepository
            .findById(documentTemplateDTO.getId())
            .map(existingDocumentTemplate -> {
                documentTemplateMapper.partialUpdate(existingDocumentTemplate, documentTemplateDTO);

                return existingDocumentTemplate;
            })
            .map(documentTemplateRepository::save)
            .map(documentTemplateMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DocumentTemplateDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all DocumentTemplates");
        return documentTemplateRepository.findAll(pageable).map(documentTemplateMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DocumentTemplateDTO> findOne(Long id) {
        LOG.debug("Request to get DocumentTemplate : {}", id);
        return documentTemplateRepository.findById(id).map(documentTemplateMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete DocumentTemplate : {}", id);
        documentTemplateRepository.deleteById(id);
    }
}
