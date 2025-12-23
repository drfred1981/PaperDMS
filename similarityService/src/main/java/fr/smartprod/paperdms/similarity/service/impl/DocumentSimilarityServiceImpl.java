package fr.smartprod.paperdms.similarity.service.impl;

import fr.smartprod.paperdms.similarity.domain.DocumentSimilarity;
import fr.smartprod.paperdms.similarity.repository.DocumentSimilarityRepository;
import fr.smartprod.paperdms.similarity.service.DocumentSimilarityService;
import fr.smartprod.paperdms.similarity.service.dto.DocumentSimilarityDTO;
import fr.smartprod.paperdms.similarity.service.mapper.DocumentSimilarityMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.similarity.domain.DocumentSimilarity}.
 */
@Service
@Transactional
public class DocumentSimilarityServiceImpl implements DocumentSimilarityService {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentSimilarityServiceImpl.class);

    private final DocumentSimilarityRepository documentSimilarityRepository;

    private final DocumentSimilarityMapper documentSimilarityMapper;

    public DocumentSimilarityServiceImpl(
        DocumentSimilarityRepository documentSimilarityRepository,
        DocumentSimilarityMapper documentSimilarityMapper
    ) {
        this.documentSimilarityRepository = documentSimilarityRepository;
        this.documentSimilarityMapper = documentSimilarityMapper;
    }

    @Override
    public DocumentSimilarityDTO save(DocumentSimilarityDTO documentSimilarityDTO) {
        LOG.debug("Request to save DocumentSimilarity : {}", documentSimilarityDTO);
        DocumentSimilarity documentSimilarity = documentSimilarityMapper.toEntity(documentSimilarityDTO);
        documentSimilarity = documentSimilarityRepository.save(documentSimilarity);
        return documentSimilarityMapper.toDto(documentSimilarity);
    }

    @Override
    public DocumentSimilarityDTO update(DocumentSimilarityDTO documentSimilarityDTO) {
        LOG.debug("Request to update DocumentSimilarity : {}", documentSimilarityDTO);
        DocumentSimilarity documentSimilarity = documentSimilarityMapper.toEntity(documentSimilarityDTO);
        documentSimilarity = documentSimilarityRepository.save(documentSimilarity);
        return documentSimilarityMapper.toDto(documentSimilarity);
    }

    @Override
    public Optional<DocumentSimilarityDTO> partialUpdate(DocumentSimilarityDTO documentSimilarityDTO) {
        LOG.debug("Request to partially update DocumentSimilarity : {}", documentSimilarityDTO);

        return documentSimilarityRepository
            .findById(documentSimilarityDTO.getId())
            .map(existingDocumentSimilarity -> {
                documentSimilarityMapper.partialUpdate(existingDocumentSimilarity, documentSimilarityDTO);

                return existingDocumentSimilarity;
            })
            .map(documentSimilarityRepository::save)
            .map(documentSimilarityMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DocumentSimilarityDTO> findOne(Long id) {
        LOG.debug("Request to get DocumentSimilarity : {}", id);
        return documentSimilarityRepository.findById(id).map(documentSimilarityMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete DocumentSimilarity : {}", id);
        documentSimilarityRepository.deleteById(id);
    }
}
