package fr.smartprod.paperdms.search.service.mapper;

import fr.smartprod.paperdms.search.domain.SemanticSearch;
import fr.smartprod.paperdms.search.service.dto.SemanticSearchDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SemanticSearch} and its DTO {@link SemanticSearchDTO}.
 */
@Mapper(componentModel = "spring")
public interface SemanticSearchMapper extends EntityMapper<SemanticSearchDTO, SemanticSearch> {}
