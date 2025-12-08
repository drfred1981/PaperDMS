package com.ged.ocr.service.mapper;

import com.ged.ocr.domain.OcrJob;
import com.ged.ocr.domain.TikaConfiguration;
import com.ged.ocr.service.dto.OcrJobDTO;
import com.ged.ocr.service.dto.TikaConfigurationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OcrJob} and its DTO {@link OcrJobDTO}.
 */
@Mapper(componentModel = "spring")
public interface OcrJobMapper extends EntityMapper<OcrJobDTO, OcrJob> {
    @Mapping(target = "tikaConfig", source = "tikaConfig", qualifiedByName = "tikaConfigurationId")
    OcrJobDTO toDto(OcrJob s);

    @Named("tikaConfigurationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TikaConfigurationDTO toDtoTikaConfigurationId(TikaConfiguration tikaConfiguration);
}
