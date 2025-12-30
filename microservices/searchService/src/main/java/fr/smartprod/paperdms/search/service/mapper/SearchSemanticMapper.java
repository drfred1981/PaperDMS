package fr.smartprod.paperdms.search.service.mapper;

import fr.smartprod.paperdms.search.domain.SearchSemantic;
import fr.smartprod.paperdms.search.service.dto.SearchSemanticDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SearchSemantic} and its DTO {@link SearchSemanticDTO}.
 */
@Mapper(componentModel = "spring")
public interface SearchSemanticMapper extends EntityMapper<SearchSemanticDTO, SearchSemantic> {}
