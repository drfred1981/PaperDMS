package com.ged.ocr.service.mapper;

import com.ged.ocr.domain.TikaConfiguration;
import com.ged.ocr.service.dto.TikaConfigurationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TikaConfiguration} and its DTO {@link TikaConfigurationDTO}.
 */
@Mapper(componentModel = "spring")
public interface TikaConfigurationMapper extends EntityMapper<TikaConfigurationDTO, TikaConfiguration> {}
