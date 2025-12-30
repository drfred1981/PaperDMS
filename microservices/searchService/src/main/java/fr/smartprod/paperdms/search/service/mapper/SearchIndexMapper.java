package fr.smartprod.paperdms.search.service.mapper;

import fr.smartprod.paperdms.search.domain.SearchIndex;
import fr.smartprod.paperdms.search.service.dto.SearchIndexDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SearchIndex} and its DTO {@link SearchIndexDTO}.
 */
@Mapper(componentModel = "spring")
public interface SearchIndexMapper extends EntityMapper<SearchIndexDTO, SearchIndex> {}
