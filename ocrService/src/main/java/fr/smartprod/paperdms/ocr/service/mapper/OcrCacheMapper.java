package fr.smartprod.paperdms.ocr.service.mapper;

import fr.smartprod.paperdms.ocr.domain.OcrCache;
import fr.smartprod.paperdms.ocr.service.dto.OcrCacheDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OcrCache} and its DTO {@link OcrCacheDTO}.
 */
@Mapper(componentModel = "spring")
public interface OcrCacheMapper extends EntityMapper<OcrCacheDTO, OcrCache> {}
