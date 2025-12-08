package com.ged.ocr.service.mapper;

import com.ged.ocr.domain.ExtractedText;
import com.ged.ocr.domain.OcrJob;
import com.ged.ocr.service.dto.ExtractedTextDTO;
import com.ged.ocr.service.dto.OcrJobDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ExtractedText} and its DTO {@link ExtractedTextDTO}.
 */
@Mapper(componentModel = "spring")
public interface ExtractedTextMapper extends EntityMapper<ExtractedTextDTO, ExtractedText> {
    @Mapping(target = "job", source = "job", qualifiedByName = "ocrJobId")
    ExtractedTextDTO toDto(ExtractedText s);

    @Named("ocrJobId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OcrJobDTO toDtoOcrJobId(OcrJob ocrJob);
}
