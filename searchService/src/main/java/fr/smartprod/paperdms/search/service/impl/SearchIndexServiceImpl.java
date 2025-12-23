package fr.smartprod.paperdms.search.service.impl;

import fr.smartprod.paperdms.search.domain.SearchIndex;
import fr.smartprod.paperdms.search.repository.SearchIndexRepository;
import fr.smartprod.paperdms.search.repository.search.SearchIndexSearchRepository;
import fr.smartprod.paperdms.search.service.SearchIndexService;
import fr.smartprod.paperdms.search.service.dto.SearchIndexDTO;
import fr.smartprod.paperdms.search.service.mapper.SearchIndexMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.search.domain.SearchIndex}.
 */
@Service
@Transactional
public class SearchIndexServiceImpl implements SearchIndexService {

    private static final Logger LOG = LoggerFactory.getLogger(SearchIndexServiceImpl.class);

    private final SearchIndexRepository searchIndexRepository;

    private final SearchIndexMapper searchIndexMapper;

    private final SearchIndexSearchRepository searchIndexSearchRepository;

    public SearchIndexServiceImpl(
        SearchIndexRepository searchIndexRepository,
        SearchIndexMapper searchIndexMapper,
        SearchIndexSearchRepository searchIndexSearchRepository
    ) {
        this.searchIndexRepository = searchIndexRepository;
        this.searchIndexMapper = searchIndexMapper;
        this.searchIndexSearchRepository = searchIndexSearchRepository;
    }

    @Override
    public SearchIndexDTO save(SearchIndexDTO searchIndexDTO) {
        LOG.debug("Request to save SearchIndex : {}", searchIndexDTO);
        SearchIndex searchIndex = searchIndexMapper.toEntity(searchIndexDTO);
        searchIndex = searchIndexRepository.save(searchIndex);
        searchIndexSearchRepository.index(searchIndex);
        return searchIndexMapper.toDto(searchIndex);
    }

    @Override
    public SearchIndexDTO update(SearchIndexDTO searchIndexDTO) {
        LOG.debug("Request to update SearchIndex : {}", searchIndexDTO);
        SearchIndex searchIndex = searchIndexMapper.toEntity(searchIndexDTO);
        searchIndex = searchIndexRepository.save(searchIndex);
        searchIndexSearchRepository.index(searchIndex);
        return searchIndexMapper.toDto(searchIndex);
    }

    @Override
    public Optional<SearchIndexDTO> partialUpdate(SearchIndexDTO searchIndexDTO) {
        LOG.debug("Request to partially update SearchIndex : {}", searchIndexDTO);

        return searchIndexRepository
            .findById(searchIndexDTO.getId())
            .map(existingSearchIndex -> {
                searchIndexMapper.partialUpdate(existingSearchIndex, searchIndexDTO);

                return existingSearchIndex;
            })
            .map(searchIndexRepository::save)
            .map(savedSearchIndex -> {
                searchIndexSearchRepository.index(savedSearchIndex);
                return savedSearchIndex;
            })
            .map(searchIndexMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SearchIndexDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all SearchIndices");
        return searchIndexRepository.findAll(pageable).map(searchIndexMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SearchIndexDTO> findOne(Long id) {
        LOG.debug("Request to get SearchIndex : {}", id);
        return searchIndexRepository.findById(id).map(searchIndexMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete SearchIndex : {}", id);
        searchIndexRepository.deleteById(id);
        searchIndexSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SearchIndexDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of SearchIndices for query {}", query);
        return searchIndexSearchRepository.search(query, pageable).map(searchIndexMapper::toDto);
    }
}
