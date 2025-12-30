package fr.smartprod.paperdms.pdftoimage.service.mapper;

import fr.smartprod.paperdms.pdftoimage.domain.ImageConversionBatch;
import fr.smartprod.paperdms.pdftoimage.service.dto.ImageConversionBatchDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ImageConversionBatch} and its DTO {@link ImageConversionBatchDTO}.
 */
@Mapper(componentModel = "spring")
public interface ImageConversionBatchMapper extends EntityMapper<ImageConversionBatchDTO, ImageConversionBatch> {}
