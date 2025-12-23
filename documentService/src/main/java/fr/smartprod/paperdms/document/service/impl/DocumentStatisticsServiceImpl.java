package fr.smartprod.paperdms.document.service.impl;

import fr.smartprod.paperdms.document.domain.DocumentStatistics;
import fr.smartprod.paperdms.document.repository.DocumentStatisticsRepository;
import fr.smartprod.paperdms.document.service.DocumentStatisticsService;
import fr.smartprod.paperdms.document.service.dto.DocumentStatisticsDTO;
import fr.smartprod.paperdms.document.service.mapper.DocumentStatisticsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.document.domain.DocumentStatistics}.
 */
@Service
@Transactional
public class DocumentStatisticsServiceImpl implements DocumentStatisticsService {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentStatisticsServiceImpl.class);

    private final DocumentStatisticsRepository documentStatisticsRepository;

    private final DocumentStatisticsMapper documentStatisticsMapper;

    public DocumentStatisticsServiceImpl(
        DocumentStatisticsRepository documentStatisticsRepository,
        DocumentStatisticsMapper documentStatisticsMapper
    ) {
        this.documentStatisticsRepository = documentStatisticsRepository;
        this.documentStatisticsMapper = documentStatisticsMapper;
    }

    @Override
    public DocumentStatisticsDTO save(DocumentStatisticsDTO documentStatisticsDTO) {
        LOG.debug("Request to save DocumentStatistics : {}", documentStatisticsDTO);
        DocumentStatistics documentStatistics = documentStatisticsMapper.toEntity(documentStatisticsDTO);
        documentStatistics = documentStatisticsRepository.save(documentStatistics);
        return documentStatisticsMapper.toDto(documentStatistics);
    }

    @Override
    public DocumentStatisticsDTO update(DocumentStatisticsDTO documentStatisticsDTO) {
        LOG.debug("Request to update DocumentStatistics : {}", documentStatisticsDTO);
        DocumentStatistics documentStatistics = documentStatisticsMapper.toEntity(documentStatisticsDTO);
        documentStatistics = documentStatisticsRepository.save(documentStatistics);
        return documentStatisticsMapper.toDto(documentStatistics);
    }

    @Override
    public Optional<DocumentStatisticsDTO> partialUpdate(DocumentStatisticsDTO documentStatisticsDTO) {
        LOG.debug("Request to partially update DocumentStatistics : {}", documentStatisticsDTO);

        return documentStatisticsRepository
            .findById(documentStatisticsDTO.getId())
            .map(existingDocumentStatistics -> {
                documentStatisticsMapper.partialUpdate(existingDocumentStatistics, documentStatisticsDTO);

                return existingDocumentStatistics;
            })
            .map(documentStatisticsRepository::save)
            .map(documentStatisticsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DocumentStatisticsDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all DocumentStatistics");
        return documentStatisticsRepository.findAll(pageable).map(documentStatisticsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DocumentStatisticsDTO> findOne(Long id) {
        LOG.debug("Request to get DocumentStatistics : {}", id);
        return documentStatisticsRepository.findById(id).map(documentStatisticsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete DocumentStatistics : {}", id);
        documentStatisticsRepository.deleteById(id);
    }
}
