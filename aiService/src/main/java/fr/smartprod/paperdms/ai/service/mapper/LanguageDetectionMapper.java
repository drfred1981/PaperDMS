package fr.smartprod.paperdms.ai.service.mapper;

import fr.smartprod.paperdms.ai.domain.LanguageDetection;
import fr.smartprod.paperdms.ai.service.dto.LanguageDetectionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link LanguageDetection} and its DTO {@link LanguageDetectionDTO}.
 */
@Mapper(componentModel = "spring")
public interface LanguageDetectionMapper extends EntityMapper<LanguageDetectionDTO, LanguageDetection> {}
