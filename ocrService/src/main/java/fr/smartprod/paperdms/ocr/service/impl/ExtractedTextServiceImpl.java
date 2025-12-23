package fr.smartprod.paperdms.ocr.service.impl;

import fr.smartprod.paperdms.ocr.domain.ExtractedText;
import fr.smartprod.paperdms.ocr.repository.ExtractedTextRepository;
import fr.smartprod.paperdms.ocr.repository.search.ExtractedTextSearchRepository;
import fr.smartprod.paperdms.ocr.service.ExtractedTextService;
import fr.smartprod.paperdms.ocr.service.dto.ExtractedTextDTO;
import fr.smartprod.paperdms.ocr.service.mapper.ExtractedTextMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.ocr.domain.ExtractedText}.
 */
@Service
@Transactional
public class ExtractedTextServiceImpl implements ExtractedTextService {

    private static final Logger LOG = LoggerFactory.getLogger(ExtractedTextServiceImpl.class);

    private final ExtractedTextRepository extractedTextRepository;

    private final ExtractedTextMapper extractedTextMapper;

    private final ExtractedTextSearchRepository extractedTextSearchRepository;

    public ExtractedTextServiceImpl(
        ExtractedTextRepository extractedTextRepository,
        ExtractedTextMapper extractedTextMapper,
        ExtractedTextSearchRepository extractedTextSearchRepository
    ) {
        this.extractedTextRepository = extractedTextRepository;
        this.extractedTextMapper = extractedTextMapper;
        this.extractedTextSearchRepository = extractedTextSearchRepository;
    }

    @Override
    public ExtractedTextDTO save(ExtractedTextDTO extractedTextDTO) {
        LOG.debug("Request to save ExtractedText : {}", extractedTextDTO);
        ExtractedText extractedText = extractedTextMapper.toEntity(extractedTextDTO);
        extractedText = extractedTextRepository.save(extractedText);
        extractedTextSearchRepository.index(extractedText);
        return extractedTextMapper.toDto(extractedText);
    }

    @Override
    public ExtractedTextDTO update(ExtractedTextDTO extractedTextDTO) {
        LOG.debug("Request to update ExtractedText : {}", extractedTextDTO);
        ExtractedText extractedText = extractedTextMapper.toEntity(extractedTextDTO);
        extractedText = extractedTextRepository.save(extractedText);
        extractedTextSearchRepository.index(extractedText);
        return extractedTextMapper.toDto(extractedText);
    }

    @Override
    public Optional<ExtractedTextDTO> partialUpdate(ExtractedTextDTO extractedTextDTO) {
        LOG.debug("Request to partially update ExtractedText : {}", extractedTextDTO);

        return extractedTextRepository
            .findById(extractedTextDTO.getId())
            .map(existingExtractedText -> {
                extractedTextMapper.partialUpdate(existingExtractedText, extractedTextDTO);

                return existingExtractedText;
            })
            .map(extractedTextRepository::save)
            .map(savedExtractedText -> {
                extractedTextSearchRepository.index(savedExtractedText);
                return savedExtractedText;
            })
            .map(extractedTextMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ExtractedTextDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all ExtractedTexts");
        return extractedTextRepository.findAll(pageable).map(extractedTextMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ExtractedTextDTO> findOne(Long id) {
        LOG.debug("Request to get ExtractedText : {}", id);
        return extractedTextRepository.findById(id).map(extractedTextMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ExtractedText : {}", id);
        extractedTextRepository.deleteById(id);
        extractedTextSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ExtractedTextDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of ExtractedTexts for query {}", query);
        return extractedTextSearchRepository.search(query, pageable).map(extractedTextMapper::toDto);
    }
}
