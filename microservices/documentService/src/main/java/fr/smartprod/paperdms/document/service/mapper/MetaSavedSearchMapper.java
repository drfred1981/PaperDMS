package fr.smartprod.paperdms.document.service.mapper;

import fr.smartprod.paperdms.document.domain.MetaSavedSearch;
import fr.smartprod.paperdms.document.service.dto.MetaSavedSearchDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MetaSavedSearch} and its DTO {@link MetaSavedSearchDTO}.
 */
@Mapper(componentModel = "spring")
public interface MetaSavedSearchMapper extends EntityMapper<MetaSavedSearchDTO, MetaSavedSearch> {}
