package com.ged.ai.service.mapper;

import com.ged.ai.domain.CorrespondentExtraction;
import com.ged.ai.service.dto.CorrespondentExtractionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CorrespondentExtraction} and its DTO {@link CorrespondentExtractionDTO}.
 */
@Mapper(componentModel = "spring")
public interface CorrespondentExtractionMapper extends EntityMapper<CorrespondentExtractionDTO, CorrespondentExtraction> {}
