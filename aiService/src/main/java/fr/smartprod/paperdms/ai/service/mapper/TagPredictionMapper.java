package fr.smartprod.paperdms.ai.service.mapper;

import fr.smartprod.paperdms.ai.domain.AutoTagJob;
import fr.smartprod.paperdms.ai.domain.TagPrediction;
import fr.smartprod.paperdms.ai.service.dto.AutoTagJobDTO;
import fr.smartprod.paperdms.ai.service.dto.TagPredictionDTO;
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
