package fr.smartprod.paperdms.ai.service.mapper;

import fr.smartprod.paperdms.ai.domain.CorrespondentExtraction;
import fr.smartprod.paperdms.ai.service.dto.CorrespondentExtractionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CorrespondentExtraction} and its DTO {@link CorrespondentExtractionDTO}.
 */
@Mapper(componentModel = "spring")
public interface CorrespondentExtractionMapper extends EntityMapper<CorrespondentExtractionDTO, CorrespondentExtraction> {}
