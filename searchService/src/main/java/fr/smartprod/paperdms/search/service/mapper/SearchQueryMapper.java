package fr.smartprod.paperdms.search.service.mapper;

import fr.smartprod.paperdms.search.domain.SearchQuery;
import fr.smartprod.paperdms.search.service.dto.SearchQueryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SearchQuery} and its DTO {@link SearchQueryDTO}.
 */
@Mapper(componentModel = "spring")
public interface SearchQueryMapper extends EntityMapper<SearchQueryDTO, SearchQuery> {}
