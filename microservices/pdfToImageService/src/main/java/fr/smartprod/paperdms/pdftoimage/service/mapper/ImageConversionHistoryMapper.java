package fr.smartprod.paperdms.pdftoimage.service.mapper;

import fr.smartprod.paperdms.pdftoimage.domain.ImageConversionHistory;
import fr.smartprod.paperdms.pdftoimage.service.dto.ImageConversionHistoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ImageConversionHistory} and its DTO {@link ImageConversionHistoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface ImageConversionHistoryMapper extends EntityMapper<ImageConversionHistoryDTO, ImageConversionHistory> {}
