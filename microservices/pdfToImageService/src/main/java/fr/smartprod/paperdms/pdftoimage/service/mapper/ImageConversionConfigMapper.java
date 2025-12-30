package fr.smartprod.paperdms.pdftoimage.service.mapper;

import fr.smartprod.paperdms.pdftoimage.domain.ImageConversionConfig;
import fr.smartprod.paperdms.pdftoimage.service.dto.ImageConversionConfigDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ImageConversionConfig} and its DTO {@link ImageConversionConfigDTO}.
 */
@Mapper(componentModel = "spring")
public interface ImageConversionConfigMapper extends EntityMapper<ImageConversionConfigDTO, ImageConversionConfig> {}
