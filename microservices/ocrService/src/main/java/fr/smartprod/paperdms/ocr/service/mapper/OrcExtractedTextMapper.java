package fr.smartprod.paperdms.ocr.service.mapper;

import fr.smartprod.paperdms.ocr.domain.OcrJob;
import fr.smartprod.paperdms.ocr.domain.OrcExtractedText;
import fr.smartprod.paperdms.ocr.service.dto.OcrJobDTO;
import fr.smartprod.paperdms.ocr.service.dto.OrcExtractedTextDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OrcExtractedText} and its DTO {@link OrcExtractedTextDTO}.
 */
@Mapper(componentModel = "spring")
public interface OrcExtractedTextMapper extends EntityMapper<OrcExtractedTextDTO, OrcExtractedText> {
    @Mapping(target = "job", source = "job", qualifiedByName = "ocrJobId")
    OrcExtractedTextDTO toDto(OrcExtractedText s);

    @Named("ocrJobId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OcrJobDTO toDtoOcrJobId(OcrJob ocrJob);
}
