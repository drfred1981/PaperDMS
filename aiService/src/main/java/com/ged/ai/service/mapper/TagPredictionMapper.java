package com.ged.ai.service.mapper;

import com.ged.ai.domain.AutoTagJob;
import com.ged.ai.domain.TagPrediction;
import com.ged.ai.service.dto.AutoTagJobDTO;
import com.ged.ai.service.dto.TagPredictionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TagPrediction} and its DTO {@link TagPredictionDTO}.
 */
@Mapper(componentModel = "spring")
public interface TagPredictionMapper extends EntityMapper<TagPredictionDTO, TagPrediction> {
    @Mapping(target = "job", source = "job", qualifiedByName = "autoTagJobId")
    TagPredictionDTO toDto(TagPrediction s);

    @Named("autoTagJobId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AutoTagJobDTO toDtoAutoTagJobId(AutoTagJob autoTagJob);
}
