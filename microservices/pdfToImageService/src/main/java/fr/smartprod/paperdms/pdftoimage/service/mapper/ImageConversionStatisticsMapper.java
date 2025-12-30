package fr.smartprod.paperdms.pdftoimage.service.mapper;

import fr.smartprod.paperdms.pdftoimage.domain.ImageConversionStatistics;
import fr.smartprod.paperdms.pdftoimage.service.dto.ImageConversionStatisticsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ImageConversionStatistics} and its DTO {@link ImageConversionStatisticsDTO}.
 */
@Mapper(componentModel = "spring")
public interface ImageConversionStatisticsMapper extends EntityMapper<ImageConversionStatisticsDTO, ImageConversionStatistics> {}
