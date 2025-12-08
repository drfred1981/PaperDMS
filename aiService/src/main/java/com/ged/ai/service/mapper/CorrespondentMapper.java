package com.ged.ai.service.mapper;

import com.ged.ai.domain.Correspondent;
import com.ged.ai.domain.CorrespondentExtraction;
import com.ged.ai.service.dto.CorrespondentDTO;
import com.ged.ai.service.dto.CorrespondentExtractionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Correspondent} and its DTO {@link CorrespondentDTO}.
 */
@Mapper(componentModel = "spring")
public interface CorrespondentMapper extends EntityMapper<CorrespondentDTO, Correspondent> {
    @Mapping(target = "extraction", source = "extraction", qualifiedByName = "correspondentExtractionId")
    CorrespondentDTO toDto(Correspondent s);

    @Named("correspondentExtractionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CorrespondentExtractionDTO toDtoCorrespondentExtractionId(CorrespondentExtraction correspondentExtraction);
}
