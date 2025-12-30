package fr.smartprod.paperdms.ai.service.mapper;

import fr.smartprod.paperdms.ai.domain.AIAutoTagJob;
import fr.smartprod.paperdms.ai.domain.AITagPrediction;
import fr.smartprod.paperdms.ai.service.dto.AIAutoTagJobDTO;
import fr.smartprod.paperdms.ai.service.dto.AITagPredictionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AITagPrediction} and its DTO {@link AITagPredictionDTO}.
 */
@Mapper(componentModel = "spring")
public interface AITagPredictionMapper extends EntityMapper<AITagPredictionDTO, AITagPrediction> {
    @Mapping(target = "job", source = "job", qualifiedByName = "aIAutoTagJobId")
    AITagPredictionDTO toDto(AITagPrediction s);

    @Named("aIAutoTagJobId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AIAutoTagJobDTO toDtoAIAutoTagJobId(AIAutoTagJob aIAutoTagJob);
}
