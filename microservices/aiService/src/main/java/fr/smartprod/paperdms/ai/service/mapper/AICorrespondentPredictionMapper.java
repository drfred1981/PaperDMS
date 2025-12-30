package fr.smartprod.paperdms.ai.service.mapper;

import fr.smartprod.paperdms.ai.domain.AIAutoTagJob;
import fr.smartprod.paperdms.ai.domain.AICorrespondentPrediction;
import fr.smartprod.paperdms.ai.service.dto.AIAutoTagJobDTO;
import fr.smartprod.paperdms.ai.service.dto.AICorrespondentPredictionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AICorrespondentPrediction} and its DTO {@link AICorrespondentPredictionDTO}.
 */
@Mapper(componentModel = "spring")
public interface AICorrespondentPredictionMapper extends EntityMapper<AICorrespondentPredictionDTO, AICorrespondentPrediction> {
    @Mapping(target = "job", source = "job", qualifiedByName = "aIAutoTagJobId")
    AICorrespondentPredictionDTO toDto(AICorrespondentPrediction s);

    @Named("aIAutoTagJobId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AIAutoTagJobDTO toDtoAIAutoTagJobId(AIAutoTagJob aIAutoTagJob);
}
