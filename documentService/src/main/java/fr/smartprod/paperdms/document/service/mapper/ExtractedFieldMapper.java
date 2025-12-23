package fr.smartprod.paperdms.document.service.mapper;

import fr.smartprod.paperdms.document.domain.ExtractedField;
import fr.smartprod.paperdms.document.service.dto.ExtractedFieldDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ExtractedField} and its DTO {@link ExtractedFieldDTO}.
 */
@Mapper(componentModel = "spring")
public interface ExtractedFieldMapper extends EntityMapper<ExtractedFieldDTO, ExtractedField> {}
