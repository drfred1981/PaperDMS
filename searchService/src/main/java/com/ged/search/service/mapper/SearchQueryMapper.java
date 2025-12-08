package com.ged.search.service.mapper;

import com.ged.search.domain.SearchQuery;
import com.ged.search.service.dto.SearchQueryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SearchQuery} and its DTO {@link SearchQueryDTO}.
 */
@Mapper(componentModel = "spring")
public interface SearchQueryMapper extends EntityMapper<SearchQueryDTO, SearchQuery> {}
