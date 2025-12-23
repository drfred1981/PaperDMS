package fr.smartprod.paperdms.document.service.impl;

import fr.smartprod.paperdms.document.domain.ExtractedField;
import fr.smartprod.paperdms.document.repository.ExtractedFieldRepository;
import fr.smartprod.paperdms.document.repository.search.ExtractedFieldSearchRepository;
import fr.smartprod.paperdms.document.service.ExtractedFieldService;
import fr.smartprod.paperdms.document.service.dto.ExtractedFieldDTO;
import fr.smartprod.paperdms.document.service.mapper.ExtractedFieldMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.document.domain.ExtractedField}.
 */
@Service
@Transactional
public class ExtractedFieldServiceImpl implements ExtractedFieldService {

    private static final Logger LOG = LoggerFactory.getLogger(ExtractedFieldServiceImpl.class);

    private final ExtractedFieldRepository extractedFieldRepository;

    private final ExtractedFieldMapper extractedFieldMapper;

    private final ExtractedFieldSearchRepository extractedFieldSearchRepository;

    public ExtractedFieldServiceImpl(
        ExtractedFieldRepository extractedFieldRepository,
        ExtractedFieldMapper extractedFieldMapper,
        ExtractedFieldSearchRepository extractedFieldSearchRepository
    ) {
        this.extractedFieldRepository = extractedFieldRepository;
        this.extractedFieldMapper = extractedFieldMapper;
        this.extractedFieldSearchRepository = extractedFieldSearchRepository;
    }

    @Override
    public ExtractedFieldDTO save(ExtractedFieldDTO extractedFieldDTO) {
        LOG.debug("Request to save ExtractedField : {}", extractedFieldDTO);
        ExtractedField extractedField = extractedFieldMapper.toEntity(extractedFieldDTO);
        extractedField = extractedFieldRepository.save(extractedField);
        extractedFieldSearchRepository.index(extractedField);
        return extractedFieldMapper.toDto(extractedField);
    }

    @Override
    public ExtractedFieldDTO update(ExtractedFieldDTO extractedFieldDTO) {
        LOG.debug("Request to update ExtractedField : {}", extractedFieldDTO);
        ExtractedField extractedField = extractedFieldMapper.toEntity(extractedFieldDTO);
        extractedField = extractedFieldRepository.save(extractedField);
        extractedFieldSearchRepository.index(extractedField);
        return extractedFieldMapper.toDto(extractedField);
    }

    @Override
    public Optional<ExtractedFieldDTO> partialUpdate(ExtractedFieldDTO extractedFieldDTO) {
        LOG.debug("Request to partially update ExtractedField : {}", extractedFieldDTO);

        return extractedFieldRepository
            .findById(extractedFieldDTO.getId())
            .map(existingExtractedField -> {
                extractedFieldMapper.partialUpdate(existingExtractedField, extractedFieldDTO);

                return existingExtractedField;
            })
            .map(extractedFieldRepository::save)
            .map(savedExtractedField -> {
                extractedFieldSearchRepository.index(savedExtractedField);
                return savedExtractedField;
            })
            .map(extractedFieldMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ExtractedFieldDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all ExtractedFields");
        return extractedFieldRepository.findAll(pageable).map(extractedFieldMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ExtractedFieldDTO> findOne(Long id) {
        LOG.debug("Request to get ExtractedField : {}", id);
        return extractedFieldRepository.findById(id).map(extractedFieldMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ExtractedField : {}", id);
        extractedFieldRepository.deleteById(id);
        extractedFieldSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ExtractedFieldDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of ExtractedFields for query {}", query);
        return extractedFieldSearchRepository.search(query, pageable).map(extractedFieldMapper::toDto);
    }
}
