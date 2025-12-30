package fr.smartprod.paperdms.ocr.service.mapper;

import fr.smartprod.paperdms.ocr.domain.OcrJob;
import fr.smartprod.paperdms.ocr.service.dto.OcrJobDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OcrJob} and its DTO {@link OcrJobDTO}.
 */
@Mapper(componentModel = "spring")
public interface OcrJobMapper extends EntityMapper<OcrJobDTO, OcrJob> {}
