package fr.smartprod.paperdms.ai.service.mapper;

import fr.smartprod.paperdms.ai.domain.AIAutoTagJob;
import fr.smartprod.paperdms.ai.domain.AILanguageDetection;
import fr.smartprod.paperdms.ai.domain.AITypePrediction;
import fr.smartprod.paperdms.ai.service.dto.AIAutoTagJobDTO;
import fr.smartprod.paperdms.ai.service.dto.AILanguageDetectionDTO;
import fr.smartprod.paperdms.ai.service.dto.AITypePredictionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AIAutoTagJob} and its DTO {@link AIAutoTagJobDTO}.
 */
@Mapper(componentModel = "spring")
public interface AIAutoTagJobMapper extends EntityMapper<AIAutoTagJobDTO, AIAutoTagJob> {
    @Mapping(target = "aITypePrediction", source = "aITypePrediction", qualifiedByName = "aITypePredictionId")
    @Mapping(target = "languagePrediction", source = "languagePrediction", qualifiedByName = "aILanguageDetectionId")
    AIAutoTagJobDTO toDto(AIAutoTagJob s);

    @Named("aITypePredictionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AITypePredictionDTO toDtoAITypePredictionId(AITypePrediction aITypePrediction);

    @Named("aILanguageDetectionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AILanguageDetectionDTO toDtoAILanguageDetectionId(AILanguageDetection aILanguageDetection);
}
