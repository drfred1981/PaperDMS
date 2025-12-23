package fr.smartprod.paperdms.search.service.impl;

import fr.smartprod.paperdms.search.domain.SemanticSearch;
import fr.smartprod.paperdms.search.repository.SemanticSearchRepository;
import fr.smartprod.paperdms.search.service.SemanticSearchService;
import fr.smartprod.paperdms.search.service.dto.SemanticSearchDTO;
import fr.smartprod.paperdms.search.service.mapper.SemanticSearchMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.search.domain.SemanticSearch}.
 */
@Service
@Transactional
public class SemanticSearchServiceImpl implements SemanticSearchService {

    private static final Logger LOG = LoggerFactory.getLogger(SemanticSearchServiceImpl.class);

    private final SemanticSearchRepository semanticSearchRepository;

    private final SemanticSearchMapper semanticSearchMapper;

    public SemanticSearchServiceImpl(SemanticSearchRepository semanticSearchRepository, SemanticSearchMapper semanticSearchMapper) {
        this.semanticSearchRepository = semanticSearchRepository;
        this.semanticSearchMapper = semanticSearchMapper;
    }

    @Override
    public SemanticSearchDTO save(SemanticSearchDTO semanticSearchDTO) {
        LOG.debug("Request to save SemanticSearch : {}", semanticSearchDTO);
        SemanticSearch semanticSearch = semanticSearchMapper.toEntity(semanticSearchDTO);
        semanticSearch = semanticSearchRepository.save(semanticSearch);
        return semanticSearchMapper.toDto(semanticSearch);
    }

    @Override
    public SemanticSearchDTO update(SemanticSearchDTO semanticSearchDTO) {
        LOG.debug("Request to update SemanticSearch : {}", semanticSearchDTO);
        SemanticSearch semanticSearch = semanticSearchMapper.toEntity(semanticSearchDTO);
        semanticSearch = semanticSearchRepository.save(semanticSearch);
        return semanticSearchMapper.toDto(semanticSearch);
    }

    @Override
    public Optional<SemanticSearchDTO> partialUpdate(SemanticSearchDTO semanticSearchDTO) {
        LOG.debug("Request to partially update SemanticSearch : {}", semanticSearchDTO);

        return semanticSearchRepository
            .findById(semanticSearchDTO.getId())
            .map(existingSemanticSearch -> {
                semanticSearchMapper.partialUpdate(existingSemanticSearch, semanticSearchDTO);

                return existingSemanticSearch;
            })
            .map(semanticSearchRepository::save)
            .map(semanticSearchMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SemanticSearchDTO> findOne(Long id) {
        LOG.debug("Request to get SemanticSearch : {}", id);
        return semanticSearchRepository.findById(id).map(semanticSearchMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete SemanticSearch : {}", id);
        semanticSearchRepository.deleteById(id);
    }
}
