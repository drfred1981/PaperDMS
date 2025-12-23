package fr.smartprod.paperdms.document.service.impl;

import fr.smartprod.paperdms.document.domain.DocumentComment;
import fr.smartprod.paperdms.document.repository.DocumentCommentRepository;
import fr.smartprod.paperdms.document.service.DocumentCommentService;
import fr.smartprod.paperdms.document.service.dto.DocumentCommentDTO;
import fr.smartprod.paperdms.document.service.mapper.DocumentCommentMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.document.domain.DocumentComment}.
 */
@Service
@Transactional
public class DocumentCommentServiceImpl implements DocumentCommentService {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentCommentServiceImpl.class);

    private final DocumentCommentRepository documentCommentRepository;

    private final DocumentCommentMapper documentCommentMapper;

    public DocumentCommentServiceImpl(DocumentCommentRepository documentCommentRepository, DocumentCommentMapper documentCommentMapper) {
        this.documentCommentRepository = documentCommentRepository;
        this.documentCommentMapper = documentCommentMapper;
    }

    @Override
    public DocumentCommentDTO save(DocumentCommentDTO documentCommentDTO) {
        LOG.debug("Request to save DocumentComment : {}", documentCommentDTO);
        DocumentComment documentComment = documentCommentMapper.toEntity(documentCommentDTO);
        documentComment = documentCommentRepository.save(documentComment);
        return documentCommentMapper.toDto(documentComment);
    }

    @Override
    public DocumentCommentDTO update(DocumentCommentDTO documentCommentDTO) {
        LOG.debug("Request to update DocumentComment : {}", documentCommentDTO);
        DocumentComment documentComment = documentCommentMapper.toEntity(documentCommentDTO);
        documentComment = documentCommentRepository.save(documentComment);
        return documentCommentMapper.toDto(documentComment);
    }

    @Override
    public Optional<DocumentCommentDTO> partialUpdate(DocumentCommentDTO documentCommentDTO) {
        LOG.debug("Request to partially update DocumentComment : {}", documentCommentDTO);

        return documentCommentRepository
            .findById(documentCommentDTO.getId())
            .map(existingDocumentComment -> {
                documentCommentMapper.partialUpdate(existingDocumentComment, documentCommentDTO);

                return existingDocumentComment;
            })
            .map(documentCommentRepository::save)
            .map(documentCommentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DocumentCommentDTO> findOne(Long id) {
        LOG.debug("Request to get DocumentComment : {}", id);
        return documentCommentRepository.findById(id).map(documentCommentMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete DocumentComment : {}", id);
        documentCommentRepository.deleteById(id);
    }
}
