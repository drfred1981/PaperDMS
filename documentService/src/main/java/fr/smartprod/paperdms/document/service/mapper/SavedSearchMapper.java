package fr.smartprod.paperdms.document.service.mapper;

import fr.smartprod.paperdms.document.domain.SavedSearch;
import fr.smartprod.paperdms.document.service.dto.SavedSearchDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SavedSearch} and its DTO {@link SavedSearchDTO}.
 */
@Mapper(componentModel = "spring")
public interface SavedSearchMapper extends EntityMapper<SavedSearchDTO, SavedSearch> {}
