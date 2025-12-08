package com.ged.search.service.impl;

import com.ged.search.domain.SearchQuery;
import com.ged.search.repository.SearchQueryRepository;
import com.ged.search.service.SearchQueryService;
import com.ged.search.service.dto.SearchQueryDTO;
import com.ged.search.service.mapper.SearchQueryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.ged.search.domain.SearchQuery}.
 */
@Service
@Transactional
public class SearchQueryServiceImpl implements SearchQueryService {

    private static final Logger LOG = LoggerFactory.getLogger(SearchQueryServiceImpl.class);

    private final SearchQueryRepository searchQueryRepository;

    private final SearchQueryMapper searchQueryMapper;

    public SearchQueryServiceImpl(SearchQueryRepository searchQueryRepository, SearchQueryMapper searchQueryMapper) {
        this.searchQueryRepository = searchQueryRepository;
        this.searchQueryMapper = searchQueryMapper;
    }

    @Override
    public SearchQueryDTO save(SearchQueryDTO searchQueryDTO) {
        LOG.debug("Request to save SearchQuery : {}", searchQueryDTO);
        SearchQuery searchQuery = searchQueryMapper.toEntity(searchQueryDTO);
        searchQuery = searchQueryRepository.save(searchQuery);
        return searchQueryMapper.toDto(searchQuery);
    }

    @Override
    public SearchQueryDTO update(SearchQueryDTO searchQueryDTO) {
        LOG.debug("Request to update SearchQuery : {}", searchQueryDTO);
        SearchQuery searchQuery = searchQueryMapper.toEntity(searchQueryDTO);
        searchQuery = searchQueryRepository.save(searchQuery);
        return searchQueryMapper.toDto(searchQuery);
    }

    @Override
    public Optional<SearchQueryDTO> partialUpdate(SearchQueryDTO searchQueryDTO) {
        LOG.debug("Request to partially update SearchQuery : {}", searchQueryDTO);

        return searchQueryRepository
            .findById(searchQueryDTO.getId())
            .map(existingSearchQuery -> {
                searchQueryMapper.partialUpdate(existingSearchQuery, searchQueryDTO);

                return existingSearchQuery;
            })
            .map(searchQueryRepository::save)
            .map(searchQueryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SearchQueryDTO> findOne(Long id) {
        LOG.debug("Request to get SearchQuery : {}", id);
        return searchQueryRepository.findById(id).map(searchQueryMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete SearchQuery : {}", id);
        searchQueryRepository.deleteById(id);
    }
}
