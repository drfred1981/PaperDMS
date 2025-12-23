package fr.smartprod.paperdms.document.service.impl;

import fr.smartprod.paperdms.document.domain.DocumentTag;
import fr.smartprod.paperdms.document.repository.DocumentTagRepository;
import fr.smartprod.paperdms.document.service.DocumentTagService;
import fr.smartprod.paperdms.document.service.dto.DocumentTagDTO;
import fr.smartprod.paperdms.document.service.mapper.DocumentTagMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.document.domain.DocumentTag}.
 */
@Service
@Transactional
public class DocumentTagServiceImpl implements DocumentTagService {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentTagServiceImpl.class);

    private final DocumentTagRepository documentTagRepository;

    private final DocumentTagMapper documentTagMapper;

    public DocumentTagServiceImpl(DocumentTagRepository documentTagRepository, DocumentTagMapper documentTagMapper) {
        this.documentTagRepository = documentTagRepository;
        this.documentTagMapper = documentTagMapper;
    }

    @Override
    public DocumentTagDTO save(DocumentTagDTO documentTagDTO) {
        LOG.debug("Request to save DocumentTag : {}", documentTagDTO);
        DocumentTag documentTag = documentTagMapper.toEntity(documentTagDTO);
        documentTag = documentTagRepository.save(documentTag);
        return documentTagMapper.toDto(documentTag);
    }

    @Override
    public DocumentTagDTO update(DocumentTagDTO documentTagDTO) {
        LOG.debug("Request to update DocumentTag : {}", documentTagDTO);
        DocumentTag documentTag = documentTagMapper.toEntity(documentTagDTO);
        documentTag = documentTagRepository.save(documentTag);
        return documentTagMapper.toDto(documentTag);
    }

    @Override
    public Optional<DocumentTagDTO> partialUpdate(DocumentTagDTO documentTagDTO) {
        LOG.debug("Request to partially update DocumentTag : {}", documentTagDTO);

        return documentTagRepository
            .findById(documentTagDTO.getId())
            .map(existingDocumentTag -> {
                documentTagMapper.partialUpdate(existingDocumentTag, documentTagDTO);

                return existingDocumentTag;
            })
            .map(documentTagRepository::save)
            .map(documentTagMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DocumentTagDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all DocumentTags");
        return documentTagRepository.findAll(pageable).map(documentTagMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DocumentTagDTO> findOne(Long id) {
        LOG.debug("Request to get DocumentTag : {}", id);
        return documentTagRepository.findById(id).map(documentTagMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete DocumentTag : {}", id);
        documentTagRepository.deleteById(id);
    }
}
