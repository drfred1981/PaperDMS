package fr.smartprod.paperdms.ai.service.mapper;

import fr.smartprod.paperdms.ai.domain.AITypePrediction;
import fr.smartprod.paperdms.ai.service.dto.AITypePredictionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AITypePrediction} and its DTO {@link AITypePredictionDTO}.
 */
@Mapper(componentModel = "spring")
public interface AITypePredictionMapper extends EntityMapper<AITypePredictionDTO, AITypePrediction> {}
