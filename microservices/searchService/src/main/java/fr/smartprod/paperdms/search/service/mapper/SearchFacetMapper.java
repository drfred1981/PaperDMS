package fr.smartprod.paperdms.search.service.mapper;

import fr.smartprod.paperdms.search.domain.SearchFacet;
import fr.smartprod.paperdms.search.domain.SearchQuery;
import fr.smartprod.paperdms.search.service.dto.SearchFacetDTO;
import fr.smartprod.paperdms.search.service.dto.SearchQueryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SearchFacet} and its DTO {@link SearchFacetDTO}.
 */
@Mapper(componentModel = "spring")
public interface SearchFacetMapper extends EntityMapper<SearchFacetDTO, SearchFacet> {
    @Mapping(target = "searchQuery", source = "searchQuery", qualifiedByName = "searchQueryId")
    SearchFacetDTO toDto(SearchFacet s);

    @Named("searchQueryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SearchQueryDTO toDtoSearchQueryId(SearchQuery searchQuery);
}
