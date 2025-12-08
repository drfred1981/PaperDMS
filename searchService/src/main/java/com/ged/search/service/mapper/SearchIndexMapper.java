package com.ged.search.service.mapper;

import com.ged.search.domain.SearchIndex;
import com.ged.search.service.dto.SearchIndexDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SearchIndex} and its DTO {@link SearchIndexDTO}.
 */
@Mapper(componentModel = "spring")
public interface SearchIndexMapper extends EntityMapper<SearchIndexDTO, SearchIndex> {}
