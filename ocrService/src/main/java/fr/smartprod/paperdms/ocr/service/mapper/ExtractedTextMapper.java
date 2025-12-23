package fr.smartprod.paperdms.ocr.service.mapper;

import fr.smartprod.paperdms.ocr.domain.ExtractedText;
import fr.smartprod.paperdms.ocr.domain.OcrJob;
import fr.smartprod.paperdms.ocr.service.dto.ExtractedTextDTO;
import fr.smartprod.paperdms.ocr.service.dto.OcrJobDTO;
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
