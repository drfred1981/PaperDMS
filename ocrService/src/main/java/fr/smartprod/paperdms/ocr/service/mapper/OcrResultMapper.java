package fr.smartprod.paperdms.ocr.service.mapper;

import fr.smartprod.paperdms.ocr.domain.OcrJob;
import fr.smartprod.paperdms.ocr.domain.OcrResult;
import fr.smartprod.paperdms.ocr.service.dto.OcrJobDTO;
import fr.smartprod.paperdms.ocr.service.dto.OcrResultDTO;
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
