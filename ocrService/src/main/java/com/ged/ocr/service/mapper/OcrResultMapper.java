package com.ged.ocr.service.mapper;

import com.ged.ocr.domain.OcrJob;
import com.ged.ocr.domain.OcrResult;
import com.ged.ocr.service.dto.OcrJobDTO;
import com.ged.ocr.service.dto.OcrResultDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OcrResult} and its DTO {@link OcrResultDTO}.
 */
@Mapper(componentModel = "spring")
public interface OcrResultMapper extends EntityMapper<OcrResultDTO, OcrResult> {
    @Mapping(target = "job", source = "job", qualifiedByName = "ocrJobId")
    OcrResultDTO toDto(OcrResult s);

    @Named("ocrJobId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OcrJobDTO toDtoOcrJobId(OcrJob ocrJob);
}
