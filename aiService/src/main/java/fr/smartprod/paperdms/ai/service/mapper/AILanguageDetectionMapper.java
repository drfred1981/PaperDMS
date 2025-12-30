package fr.smartprod.paperdms.ai.service.mapper;

import fr.smartprod.paperdms.ai.domain.AILanguageDetection;
import fr.smartprod.paperdms.ai.service.dto.AILanguageDetectionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AILanguageDetection} and its DTO {@link AILanguageDetectionDTO}.
 */
@Mapper(componentModel = "spring")
public interface AILanguageDetectionMapper extends EntityMapper<AILanguageDetectionDTO, AILanguageDetection> {}
