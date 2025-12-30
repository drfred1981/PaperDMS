package fr.smartprod.paperdms.ocr.service.mapper;

import fr.smartprod.paperdms.ocr.domain.OcrComparison;
import fr.smartprod.paperdms.ocr.service.dto.OcrComparisonDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OcrComparison} and its DTO {@link OcrComparisonDTO}.
 */
@Mapper(componentModel = "spring")
public interface OcrComparisonMapper extends EntityMapper<OcrComparisonDTO, OcrComparison> {}
