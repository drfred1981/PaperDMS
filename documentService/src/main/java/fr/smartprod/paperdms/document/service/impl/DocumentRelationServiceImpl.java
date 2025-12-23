package fr.smartprod.paperdms.document.service.impl;

import fr.smartprod.paperdms.document.domain.DocumentRelation;
import fr.smartprod.paperdms.document.repository.DocumentRelationRepository;
import fr.smartprod.paperdms.document.service.DocumentRelationService;
import fr.smartprod.paperdms.document.service.dto.DocumentRelationDTO;
import fr.smartprod.paperdms.document.service.mapper.DocumentRelationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.document.domain.DocumentRelation}.
 */
@Service
@Transactional
public class DocumentRelationServiceImpl implements DocumentRelationService {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentRelationServiceImpl.class);

    private final DocumentRelationRepository documentRelationRepository;

    private final DocumentRelationMapper documentRelationMapper;

    public DocumentRelationServiceImpl(
        DocumentRelationRepository documentRelationRepository,
        DocumentRelationMapper documentRelationMapper
    ) {
        this.documentRelationRepository = documentRelationRepository;
        this.documentRelationMapper = documentRelationMapper;
    }

    @Override
    public DocumentRelationDTO save(DocumentRelationDTO documentRelationDTO) {
        LOG.debug("Request to save DocumentRelation : {}", documentRelationDTO);
        DocumentRelation documentRelation = documentRelationMapper.toEntity(documentRelationDTO);
        documentRelation = documentRelationRepository.save(documentRelation);
        return documentRelationMapper.toDto(documentRelation);
    }

    @Override
    public DocumentRelationDTO update(DocumentRelationDTO documentRelationDTO) {
        LOG.debug("Request to update DocumentRelation : {}", documentRelationDTO);
        DocumentRelation documentRelation = documentRelationMapper.toEntity(documentRelationDTO);
        documentRelation = documentRelationRepository.save(documentRelation);
        return documentRelationMapper.toDto(documentRelation);
    }

    @Override
    public Optional<DocumentRelationDTO> partialUpdate(DocumentRelationDTO documentRelationDTO) {
        LOG.debug("Request to partially update DocumentRelation : {}", documentRelationDTO);

        return documentRelationRepository
            .findById(documentRelationDTO.getId())
            .map(existingDocumentRelation -> {
                documentRelationMapper.partialUpdate(existingDocumentRelation, documentRelationDTO);

                return existingDocumentRelation;
            })
            .map(documentRelationRepository::save)
            .map(documentRelationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DocumentRelationDTO> findOne(Long id) {
        LOG.debug("Request to get DocumentRelation : {}", id);
        return documentRelationRepository.findById(id).map(documentRelationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete DocumentRelation : {}", id);
        documentRelationRepository.deleteById(id);
    }
}
